package com.example;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.example.PubUtil.Util;
import com.example.entity.ModelFiles;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.*;
import java.util.*;
/**
 *
 */
import static java.util.stream.Collectors.groupingBy;

@SpringBootTest
class JsonCreateApplicationTests {

    private static List<ModelFiles> modelFilesList = new ArrayList<>();

    private JSONArray buildingGroupArray = new JSONArray();

    @Test
    void contextLoads() throws IOException {
        readExcel();
        JSONObject jsonObject = new JSONObject(true);
        JSONObject scene = new JSONObject(true);
        scene.put("guid", UUID.randomUUID().toString());
        scene.put("name", "Park 3D small scene");
        scene.put("font", "SimHei_Regular");
        scene.put("model_def_group", modelDefGroup());
        scene.put("ground_group", GroundGroup());
        List<ModelFiles> modelFilesList2 = new ArrayList<>();
        String a = modelFilesList.get(0).getModelName().substring(6,9);
        String b = null;
        for (ModelFiles mod : modelFilesList) {
            b = mod.getModelName().substring(6,9);
            if( b.equals(a)){
                modelFilesList2.add(mod);
            }else{
                scene.put("building_group", buildingGroup(modelFilesList2));

                a = b;
                modelFilesList2.clear();
                modelFilesList2.add(mod);
            }
        }

        scene.put("building_group", buildingGroup(modelFilesList2));
        jsonObject.put("scene", scene);
        System.out.println(jsonObject);

        File file = new File("D:\\scene_000001.json");
        if (!file.getParentFile().exists())
        { // 如果父目录不存在，创建父目录
            file.getParentFile().mkdirs();
        }
        if (file.exists()) { // 如果已存在,删除旧文件
            file.delete();
        }
        file.createNewFile();
        String formatJson = formatJson(jsonObject.toString());
        // 将格式化后的字符串写入文件
        Writer write = new OutputStreamWriter(new FileOutputStream(file), "UTF-8");
        write.write(formatJson);
        write.flush();
        write.close();
    }

    /**
     * 读取excel
     * @throws IOException
     */
    public void readExcel() throws IOException {
        Workbook workbook = new XSSFWorkbook(new FileInputStream("D:\\model.xlsx"));
        Sheet sheet = workbook.getSheetAt(0);
            //  1.总行数
        int rows = sheet.getPhysicalNumberOfRows();
        for (int i = 1; i < rows; i++) {
            ModelFiles modelFiles = new ModelFiles();
            Row row = sheet.getRow(i);
            //  2.模型编号
            Cell modelNoCell = row.getCell(0);
            modelNoCell.setCellType(CellType.STRING);
            String modelNo = modelNoCell.getStringCellValue();
            modelFiles.setModelNo(modelNo);
            if(modelNo.length()>=5){
                String groupNo = modelNo.substring(0, 3);
                modelFiles.setGroupNo(Integer.valueOf(groupNo));
                if(Integer.parseInt(modelNo.substring(3,5)) >= 10){
                    String floorNo = modelNo.substring(3,5);
                    modelFiles.setFloorNo(floorNo);
                }else {
                    String floorNo = modelNo.substring(4,5);
                    modelFiles.setFloorNo(floorNo);
                }
            } else {
                String groupNo = modelNo.substring(0, 2);
                modelFiles.setGroupNo(Integer.valueOf(groupNo));
                String floorNo = modelNo.substring(2);
                modelFiles.setFloorNo(floorNo);
            }
            //  3.文件名称
            Cell modelNameCell = row.getCell(1);
            modelNameCell.setCellType(CellType.STRING);
            String modelName = modelNameCell.getStringCellValue();
            modelFiles.setModelName(modelName);
            String modelType = modelName.substring(0, modelName.lastIndexOf("_"));
            modelFiles.setModelType(modelType.toUpperCase());
            //  4.x
            Cell xCell = row.getCell(2);
            xCell.setCellType(CellType.STRING);
            String x = xCell.getStringCellValue();
            modelFiles.setX(Double.valueOf(x));
            // 5.y
            Cell yCell = row.getCell(3);
            yCell.setCellType(CellType.STRING);
            String y = yCell.getStringCellValue();
            modelFiles.setY(Double.valueOf(y));
            // 6.z
            Cell zCell = row.getCell(4);
            zCell.setCellType(CellType.STRING);
            String z = zCell.getStringCellValue();
            modelFiles.setZ(Double.valueOf(z));
            // 7.height
            Cell heightCell = row.getCell(5);
            heightCell.setCellType(CellType.STRING);
            String height = heightCell.getStringCellValue();
            modelFiles.setHeight(Double.valueOf(height));
            modelFilesList.add(modelFiles);
        }
        workbook.close();
    }
    /**
     * model_def_group
     *
     * @return
     */

    public JSONArray modelDefGroup() {
        JSONArray modelDefGroupArray = new JSONArray();
        JSONObject ground_000001 = new JSONObject(true);
        ground_000001.put("guid", "83477573-b795-4ecd-bc45-a2d108f70f96");
        ground_000001.put("name", "ground_000001");
        ground_000001.put("file", "ground_000001");
        ground_000001.put("type", "OBJ");
        for (ModelFiles modelFiles : modelFilesList) {
                JSONObject modelDefGroupObject = new JSONObject(true);
                modelDefGroupObject.put("guid", "4f19ba55-9e68-4bb3-" + modelFiles.getModelNo() + "-57765e5c1a49");
                modelDefGroupObject.put("name", modelFiles.getModelName());
                modelDefGroupObject.put("file", modelFiles.getModelName());
                modelDefGroupObject.put("type", "OBJ");
                modelDefGroupArray.add(modelDefGroupObject);
            }
        return modelDefGroupArray;
        }
    /**
     * ground_group
     *
     * @return
     */
    public JSONArray GroundGroup() {
        JSONArray groundGroupArray = new JSONArray();
        JSONObject groundGroupObject = new JSONObject(true);
        groundGroupObject.put("guid", "2782080e-d02f-4525-98c4-ce8e3c455b79");
        groundGroupObject.put("name", "01");
        groundGroupObject.put("no", 1);
        JSONArray modelRefGroupArray = new JSONArray();
        JSONObject modelRefGroupObject = new JSONObject(true);
        modelRefGroupObject.put("type", "GROUND");
        modelRefGroupObject.put("model_def_guid", "83477573-b795-4ecd-bc45-a2d108f70f96");
        JSONObject modelRefGroupTransformObject = new JSONObject(true);
        JSONObject modelRefGroupTransformTranslateObject = new JSONObject(true);
        modelRefGroupTransformTranslateObject.put("x", 0.0);
        modelRefGroupTransformTranslateObject.put("y", 0.0);
        modelRefGroupTransformTranslateObject.put("z", 0.0);
        modelRefGroupTransformObject.put("translate", modelRefGroupTransformTranslateObject);
        modelRefGroupTransformObject.put("rotate", 0.0);
        modelRefGroupTransformObject.put("scale", 1.0);
        modelRefGroupObject.put("transform", modelRefGroupTransformObject);
        modelRefGroupArray.add(modelRefGroupObject);
        groundGroupObject.put("model_ref_group", modelRefGroupArray);
        JSONObject groundGroupTransformObject = new JSONObject(true);
        JSONObject groundGroupTransformTranslateObject = new JSONObject(true);
        groundGroupTransformTranslateObject.put("x", 0.0);
        groundGroupTransformTranslateObject.put("y", 0.0);
        groundGroupTransformTranslateObject.put("z", 0.0);
        groundGroupTransformObject.put("translate", groundGroupTransformTranslateObject);
        groundGroupTransformObject.put("rotate", 0.0);
        groundGroupTransformObject.put("scale", 1.0);
        groundGroupObject.put("transform", groundGroupTransformObject);
        groundGroupArray.add(groundGroupObject);
        return groundGroupArray;
    }
    /**
     * building_group
     *
     * @return
     */
    public JSONArray buildingGroup(List<ModelFiles> modelFilesList1) {

        Map<Integer, List<ModelFiles>> map = modelFilesList1.stream().collect(groupingBy(ModelFiles::getGroupNo));
        Set<Map.Entry<Integer, List<ModelFiles>>> entries = map.entrySet();
        for (Map.Entry<Integer, List<ModelFiles>> entry : entries) {
            Double x = 0.0;
            Double y = 0.0;
            Double z = 0.0;
            Integer key = entry.getKey();
            List<ModelFiles> value = entry.getValue();
            JSONObject buildingGroupObject = new JSONObject(true);
            buildingGroupObject.put("guid", "b767b7e2-ff14-4a19-00" + key + "-fb4794ef7463");
            buildingGroupObject.put("name", "");
            buildingGroupObject.put("no", key);
            JSONArray layerGroupArray = new JSONArray();
            for (ModelFiles modelFiles : value) {
                    x = modelFiles.getX();
                    y = modelFiles.getY();
                    z = modelFiles.getZ();
                    Integer floorNo = Integer.valueOf(modelFiles.getFloorNo());
                    JSONObject layerGroupObject = new JSONObject(true);
                    layerGroupObject.put("guid", new String[]{"7225a9d6-c222-4d05-" + modelFiles.getModelNo() + "-71a8d77f93e0"});
                    layerGroupObject.put("name", new String[]{modelFiles.getFloorNo()});
                    layerGroupObject.put("no", new Integer[]{Integer.valueOf(modelFiles.getFloorNo())});
                    if (floorNo > 1) {
//                   double high = modelFiles.getHeight()/(floorNo-1);
                        layerGroupObject.put("height", new Double[]{(modelFiles.getHeight()) / (floorNo - 1)});
                    } else {
                        layerGroupObject.put("height", new Double[]{modelFiles.getHeight()});
                    }
//                layerGroupObject.put("height", new Double[]{modelFiles.getHeight()});
                    JSONArray modelRefGroupArray = new JSONArray();
                    JSONObject modelRefGroupObject = new JSONObject(true);
                    modelRefGroupObject.put("type", modelFiles.getModelType());
                    modelRefGroupObject.put("model_def_guid", "4f19ba55-9e68-4bb3-" + modelFiles.getModelNo() + "-57765e5c1a49");
                    JSONObject transformObject = new JSONObject(true);
                    JSONObject translateObject = new JSONObject(true);
                    translateObject.put("x", 0.0);
                    translateObject.put("y", 0.0);
                    translateObject.put("z", 0.0);
                    transformObject.put("translate", translateObject);
                    transformObject.put("rotate", 0.0);
                    transformObject.put("scale", 1.0);
                    modelRefGroupObject.put("transform", transformObject);
                    modelRefGroupArray.add(modelRefGroupObject);
                    layerGroupObject.put("model_ref_group", modelRefGroupArray);
                    layerGroupArray.add(layerGroupObject);
                    buildingGroupObject.put("layer_group", layerGroupArray);
                    JSONObject transformObject1 = new JSONObject(true);
                    JSONObject translateObject1 = new JSONObject(true);
                    translateObject1.put("x", x);
                    translateObject1.put("y", y);
                    translateObject1.put("z", z);
                    transformObject1.put("translate", translateObject1);
                    transformObject1.put("rotate", 0.0);
                    transformObject1.put("scale", 1.0);
                    buildingGroupObject.put("transform", transformObject1);
                    buildingGroupArray.add(buildingGroupObject);
//                }
            }
        }
        return buildingGroupArray;
    }
    /**
     * 单位缩进字符串。
     */
    private static String SPACE = "   ";

    /**
     * 返回格式化JSON字符串。
     *
     * @param json 未格式化的JSON字符串。
     * @return 格式化的JSON字符串。
     */
    public static String formatJson(String json) {
        StringBuffer result = new StringBuffer();

        int length = json.length();
        int number = 0;
        char key = 0;

        // 遍历输入字符串。
        for (int i = 0; i < length; i++) {
            // 1、获取当前字符。
            key = json.charAt(i);

            // 2、如果当前字符是前方括号、前花括号做如下处理：
            if ((key == '[') || (key == '{')) {
                // （1）如果前面还有字符，并且字符为“：”，打印：换行和缩进字符字符串。
                if ((i - 1 > 0) && (json.charAt(i - 1) == ':')) {
                    result.append('\n');
                    result.append(indent(number));
                }

                // （2）打印：当前字符。
                result.append(key);

                // （3）前方括号、前花括号，的后面必须换行。打印：换行。
                result.append('\n');

                // （4）每出现一次前方括号、前花括号；缩进次数增加一次。打印：新行缩进。
                number++;
                result.append(indent(number));

                // （5）进行下一次循环。
                continue;
            }

            // 3、如果当前字符是后方括号、后花括号做如下处理：
            if ((key == ']') || (key == '}')) {
                // （1）后方括号、后花括号，的前面必须换行。打印：换行。
                result.append('\n');

                // （2）每出现一次后方括号、后花括号；缩进次数减少一次。打印：缩进。
                number--;
                result.append(indent(number));

                // （3）打印：当前字符。
                result.append(key);

                // （4）如果当前字符后面还有字符，并且字符不为“，”，打印：换行。
                if (((i + 1) < length) && (json.charAt(i + 1) != ',')) {
                    result.append('\n');
                }

                // （5）继续下一次循环。
                continue;
            }

            // 4、如果当前字符是逗号。逗号后面换行，并缩进，不改变缩进次数。
            if ((key == ',')) {
                result.append(key);
                result.append('\n');
                result.append(indent(number));
                continue;
            }

            // 5、打印：当前字符。
            result.append(key);
        }

        return result.toString();
    }

    /**
     * 返回指定次数的缩进字符串。每一次缩进三个空格，即SPACE。
     *
     * @param number 缩进次数。
     * @return 指定缩进次数的字符串。
     */
    private static String indent(int number) {
        StringBuffer result = new StringBuffer();
        for (int i = 0; i < number; i++) {
            result.append(SPACE);
        }
        return result.toString();
    }

}
