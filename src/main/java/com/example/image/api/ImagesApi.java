package com.example.image.api;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.example.image.model.GoodsModel;
import com.example.image.utils.DownloadPicFromUtils;
import com.example.image.utils.HttpUtil;
import com.example.image.utils.commonUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.File;
import java.io.IOException;
import java.util.*;

/**
 * @author Administrator
 * @date 2020/2/22 18:22
 */
@Controller
public class ImagesApi {

    private String url = "https://h5api.m.taobao.com/h5/mtop.taobao.detail.getdesc/6.0/?data=";

    private String goodsUrl = "https://h5api.m.taobao.com/h5/mtop.taobao.detail.getdetail/6.0/?jsv=2.4.8&appKey=12574478&t=1564819317695&sign=34a3c03c59120926de47b25923cc1380&api=mtop.taobao.detail.getdetail&v=6.0&dataType=jsonp&ttid=2017@taobao_h5_6.6.0&AntiCreep=true&type=jsonp&callback=mtopjsonp2&data=";

    /**
     * 查看
     *
     * @param id
     * @param model
     * @return
     * @throws IOException
     */
    @GetMapping("/")
    public Object selectAll(@RequestParam(value = "id", required = false) String id, Model model) throws IOException {
        if (StringUtils.isBlank(id)) {
            model.addAttribute("detailsFigure", null);
            model.addAttribute("goodsDetailed", null);
            return "show";
        }
        List<String> detailedPictureList = getDetailedPicture(id);
        GoodsModel goodsDetailed = getGoodsDetailed(id);

        model.addAttribute("goodsDetailed", goodsDetailed);
        model.addAttribute("detailsFigure", detailedPictureList);
        model.addAttribute("goodsId", id);
        return "show";
    }

    /**
     * 获取页面详情图
     *
     * @param id
     * @return
     */
    private List<String> getDetailedPicture(String id) {
        String param = commonUtils.encodeUrlComponent("{\"id\":\"" + id + "\",\"type\":\"0\"}");
        String json = HttpUtil.httpReturnJson(url + param, null);
        JSONObject jsonObject = JSON.parseObject(json);
        JSONObject data = jsonObject.getJSONObject("data").getJSONObject("wdescContent");
        JSONArray pages = data.getJSONArray("pages");
        List<String> list = new ArrayList<>();
        for (int i = 0; i < pages.size() - 1; i++) {
            String image = pages.getString(i);
            String correctImage = commonUtils.httpUrl(commonUtils.splitimage(image));
            list.add(correctImage);
        }
        return list;
    }

    /**
     * 获取商品基本信息
     *
     * @param id
     * @return
     */
    private GoodsModel getGoodsDetailed(String id) {
        String param = commonUtils.encodeUrlComponent("{\"itemNumId\":\"" + id + "\"}");
        String json = HttpUtil.httpReturnJson(goodsUrl + param, null);
        try {
            json = json.replace("mtopjsonp2(", "");
            json = json.substring(0, json.length() - 1);
        } catch (Exception e) {
            e.printStackTrace();
        }
        JSONObject data = JSON.parseObject(json).getJSONObject("data").getJSONObject("item");

        GoodsModel goodsModel = new GoodsModel();
        JSONArray images = data.getJSONArray("images");
        List<String> list = new ArrayList<>();
        for (int i = 0; i < images.size() - 1; i++) {
            String image = images.getString(i);
            String correctImage = commonUtils.httpUrl(image);
            list.add(correctImage);
        }
        JSONObject basicInfo = JSON.parseObject(json).getJSONObject("data").getJSONObject("props");
        goodsModel.setBasicInfo(basicInfo.toString());
        goodsModel.setTitle(data.getString("title"));
        goodsModel.setId(data.getString("itemId"));
        goodsModel.setImages(list);
        return goodsModel;
    }

    /**
     * 下载
     *
     * @param id
     * @param model
     * @return
     */
    @GetMapping("download")
    @ResponseBody
    public Object download(@RequestParam(value = "id", required = false) String id, Model model) {
        Map<String, String> map = new HashMap<>();
        if (StringUtils.isBlank(id)) {
            map.put("code", "500");
            return map;
        }
        String param = commonUtils.encodeUrlComponent("{\"id\":\"" + id + "\",\"type\":\"0\"}");
        String json = HttpUtil.httpReturnJson(url + param, null);
        JSONObject jsonObject = JSON.parseObject(json);
        JSONObject data = jsonObject.getJSONObject("data").getJSONObject("wdescContent");
        JSONArray pages = data.getJSONArray("pages");

        GoodsModel goodsDetailed = getGoodsDetailed(id);

        //自定义保存的文件夹名称
        String date = DateFormatUtils.format(new Date(), "yyyy-MM-dd-hh-mm-ss");
        //参数为空
        File file = null;
        try {
            file = new File(new File("").getCanonicalPath() + "/" + date);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (!file.exists()) {
            //创建文件夹
            file.mkdir();
        }
        List<String> images = goodsDetailed.getImages();
        for (int j = 0; j < images.size(); j++) {
            String image = images.get(j);
            try {
                DownloadPicFromUtils.downloadPicture(image, j, 0, file);
            } catch (IOException e) {
                map.put("code", "500");
                return map;
            }
        }
        for (int i = 0; i < pages.size() - 1; i++) {
            String image = pages.getString(i);
            String correctImage = commonUtils.httpUrl(commonUtils.splitimage(image));
            try {
                DownloadPicFromUtils.downloadPicture(correctImage, i, 1, file);
            } catch (IOException e) {
                map.put("code", "500");
                return map;
            }
        }
        map.put("code", "200");
        return map;
    }

    public static void main(String[] args) {
        String u = "https://h5api.m.taobao.com/h5/mtop.taobao.detail.getdetail/6.0/?jsv=2.4.8&appKey=12574478&t=1564819317695&sign=34a3c03c59120926de47b25923cc1380&api=mtop.taobao.detail.getdetail&v=6.0&dataType=jsonp&ttid=2017@taobao_h5_6.6.0&AntiCreep=true&type=jsonp&callback=mtopjsonp2&data=";
        String param = commonUtils.encodeUrlComponent("{\"itemNumId\":\"607895749938\"}");
        String json = HttpUtil.httpReturnJson(u + param, null);
        try {
            json = json.replace("mtopjsonp2(", "");
            json = json.substring(0, json.length() - 1);
        } catch (Exception e) {
            e.printStackTrace();
        }
        JSONObject jsonObject = JSON.parseObject(json).getJSONObject("data").getJSONObject("item");
        System.out.println(jsonObject);
    }
}
