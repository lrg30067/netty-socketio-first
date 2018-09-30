package com.sinovoice.hcicloud.nettysocketiofirst;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.junit.Test;

import java.util.*;

@Slf4j
public class JsonTest {
    @Test
    public void jsonTest() throws Exception {
        //language=JSON
        String jsonString = "{\n" +
                "  \"id\": \"15013\",\n" +
                "  \"talkertype\": \"1\",\n" +
                "  \"content\": \"好 的 啊 那 感谢 宝贵 时间 呢 确保 服务质量 通话录音 那 首先 第一项 服务 呢 就 比较 高端 咱们 考虑到 客户 平时 工作 生活 资金周转 量 就 比较 大 那么 给 到 您 这边 呢 就 通知 到 一个 无 抵押 免 担保 的 信用 贷款 服务 也就是说 后期 呀 张 小姐 您 这边 不管 是 换车 买房 做生意 都 好 遇到;time=139940 160070\",\n" +
                "  \"timestamp\": 1535613514040,\n" +
                "  \"time\": \"15:18:34\",\n" +
                "  \"cmd\": \"\",\n" +
                "  \"userphone\": \"018818550728\",\n" +
                "  \"channelId\": \"8014b366ac2411e8\",\n" +
                "  \"reminding\": \"\",\n" +
                "  \"answer\": \"\",\n" +
                "  \"answerType\": \"3\",\n" +
                "  \"forbiddenWord\": \"\"\n" +
                "}";
        JSONObject jsonObject = JSONObject.parseObject(jsonString);
        String content = jsonObject.getString("content");

        String contentNew = content.substring(0, content.indexOf(";time="));
        if (contentNew != null && contentNew.trim().length() > 0 && contentNew != "\"\"") {
            String speakContent = content.substring(0, content.indexOf(";time="));
            String timeStr = content.substring(content.indexOf(";time=") + 6, content.length());
            log.info(speakContent);
            log.info("时间为：{}", timeStr);

            log.info(speakContent.replace(" ", ""));
        }
    }


    @Test
    public void map_string_list_json_Test() throws Exception {
        // 生成json
        HashMap<String, List<String>> mapTa = new HashMap<>();
        log.info(JSON.toJSONString(mapTa));

        List<String> list = new ArrayList<>();
        list.add("贷款十万[(2018-09-07 15:22:50),(27:12)]");
//        list.add("mhn[(2018-09-07 15:22:50),(27:12)][已纠正]");

        mapTa.put("提及贷款", list);
        mapTa.put("客户问题", list);
        String jiexi = JSON.toJSONString(mapTa);
        log.info(jiexi);
        log.info(JSONArray.toJSONString(mapTa));


        // 解析json
        JSONArray textnArray = new JSONArray();
        if (StringUtils.isNotEmpty(jiexi)) {
            Map<String, List<String>> mapF = (Map<String, List<String>>) JSON.parse(jiexi);
            for (Map.Entry<String, List<String>> entry : mapF.entrySet()) {
                JSONObject textObj = new JSONObject();
                textObj.put("rulename", entry.getKey().replaceAll("pattern_", ""));

                String content = entry.getValue().get(0);
                String hitPart = "";
                String hitPartTime = "";
                String mark = "";

                int beginIndexTime = content.indexOf("[");
                int middleIndexTime = content.indexOf("][");
                if (beginIndexTime > 0) {
                    hitPart = content.substring(0, beginIndexTime);
                    if (middleIndexTime > 0) {
                        hitPartTime = content.substring(beginIndexTime + 1, middleIndexTime + 1 - 1);
                        mark = content.substring(middleIndexTime + 1, content.length());
                    } else {
                        hitPartTime = content.substring(beginIndexTime + 1, content.length() - 1);
                    }
                }
                textObj.put("hitPart", hitPart);
                textObj.put("hitPartTime", hitPartTime);
                textObj.put("mark", mark);
                textnArray.add(textObj);
            }
        }
        log.info(textnArray.toJSONString());
    }


}
