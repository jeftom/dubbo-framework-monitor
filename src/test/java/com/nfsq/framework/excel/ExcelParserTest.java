package com.nfsq.framework.excel;

import org.apache.poi.openxml4j.exceptions.OpenXML4JException;
import org.junit.Test;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;

/**
 * Created by guoyongzheng on 15/6/18.
 */
public class ExcelParserTest {

    @Test
    public void testParseXlsx() throws IOException, SAXException, OpenXML4JException, ParserConfigurationException {
        InputStream file = ExcelParserTest.class.getClassLoader().getResourceAsStream("新模式运营城市现有终端客户固定配送片区绑定表.xlsx");

        Map<Integer, ExcelParseHandler> handlers = new HashMap<>();
        handlers.put(0, new TestHandler());
        handlers.put(1, new TestHandler());
        handlers.put(2, new TestHandler());
        ExcelParser parser = new ExcelParser(handlers);

        parser.parseXlsx(file);

        TestHandler h1 = (TestHandler)handlers.get(0);
        assertEquals(13113, h1.getRows());
        Map<Integer, String> map1 = h1.getMap();
        assertEquals(13113, map1.size());
        assertEquals("NFHZ05710012; 滨兴; 浙江省; 杭州市; 滨江区; 江汉东路钱塘滨和花园8幢1单元701; 周; 先生; 89890778; ", map1.get(13113));

        TestHandler h2 = (TestHandler)handlers.get(1);
        assertEquals(8818, h2.getRows());
        Map<Integer, String> map2 = h2.getMap();
        assertEquals(8818, map2.size());
        assertEquals("NFHZ05710012; 滨兴; 浙江省; 杭州市; 滨江区; 江汉东路钱塘滨和花园8幢1单元701; 周; 先生; 89890778; ", map2.get(8818));

        TestHandler h3 = (TestHandler)handlers.get(2);
        assertEquals(1367, h3.getRows());
        Map<Integer, String> map3 = h3.getMap();
        assertEquals(1367, map3.size());
        assertEquals("NFHZ05920008; 6号区; 福建省; 厦门市; 思明区; 曾错安西边-63-号1楼102室; 杨丽芳; 先生; 13306031363; ", map3.get(1367));
    }

    private static class TestHandler extends ExcelParseHandler{

        private int rows = 0;

        private StringBuilder sb = new StringBuilder();

        private Map<Integer, String> map = new HashMap<>();

        public int getRows() {
            return rows;
        }

        public Map<Integer, String> getMap() {
            return map;
        }

        /**
         * 开始一行
         */
        @Override
        public void startRow(int row) {
        }

        /**
         * 结束一行
         *
         * @param row
         */
        @Override
        public void endRow(int row) {
            if (sb.length() > 0) {
                map.put(row, sb.toString());
                sb = new StringBuilder();
            }
        }

        /**
         * 处理每列
         *
         * @param content
         */
        @Override
        public void handleA(String content) {
            this.rows++;

            sb.append(content).append("; ");
        }

        @Override
        public void handleB(String content) {
            sb.append(content).append("; ");
        }

        @Override
        public void handleC(String content) {
            sb.append(content).append("; ");
        }

        @Override
        public void handleD(String content) {
            sb.append(content).append("; ");
        }

        @Override
        public void handleE(String content) {
            sb.append(content).append("; ");
        }

        @Override
        public void handleF(String content) {
            sb.append(content).append("; ");
        }

        @Override
        public void handleG(String content) {
            sb.append(content).append("; ");
        }

        @Override
        public void handleH(String content) {
            sb.append(content).append("; ");
        }

        @Override
        public void handleI(String content) {
            sb.append(content).append("; ");
        }

        @Override
        public void handleJ(String content) {
            sb.append(content).append("; ");
        }

        @Override
        public void handleK(String content) {
            super.handleK(content);
        }

        @Override
        public void handleL(String content) {
            super.handleL(content);
        }

        @Override
        public void handleM(String content) {
            super.handleM(content);
        }

        @Override
        public void handleN(String content) {
            super.handleN(content);
        }

        @Override
        public void handleO(String content) {
            super.handleO(content);
        }

        @Override
        public void handleP(String content) {
            super.handleP(content);
        }

        @Override
        public void handleQ(String content) {
            super.handleQ(content);
        }

        @Override
        public void handleR(String content) {
            super.handleR(content);
        }

        @Override
        public void handleS(String content) {
            super.handleS(content);
        }

        @Override
        public void handleT(String content) {
            super.handleT(content);
        }

        @Override
        public void handleU(String content) {
            super.handleU(content);
        }

        @Override
        public void handleV(String content) {
            super.handleV(content);
        }

        @Override
        public void handleW(String content) {
            super.handleW(content);
        }

        @Override
        public void handleX(String content) {
            super.handleX(content);
        }

        @Override
        public void handleY(String content) {
            super.handleY(content);
        }

        @Override
        public void handleZ(String content) {
            super.handleZ(content);
        }
    }
}
