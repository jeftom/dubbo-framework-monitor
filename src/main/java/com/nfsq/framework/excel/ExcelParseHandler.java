package com.nfsq.framework.excel;

/**
 * Created by guoyongzheng on 15/6/18.
 */
public class ExcelParseHandler {

    /**
     * 是否略过一行
     */
    private boolean skip = false;

    /**
     * 标记略过该行
     */
    protected void skipRow() {
        this.skip = true;
    }

    /**
     * 标记被略过的行已经结束
     */
    protected void endSkipRow() {
        this.skip = false;
    }

    /**
     * 该行是否略过
     * @return
     */
    protected boolean isSkip() {
        return this.skip;
    }

    /**
     * 开始一行
     */
    public void startRow(int row) {

    }

    /**
     * 结束一行
     */
    public void endRow(int row) {

    }

    /**
     * 处理每列
     */
    public void handleA(String content) {

    }

    public void handleB(String content) {

    }

    public void handleC(String content) {

    }

    public void handleD(String content) {

    }

    public void handleE(String content) {

    }

    public void handleF(String content) {

    }

    public void handleG(String content) {

    }

    public void handleH(String content) {

    }

    public void handleI(String content) {

    }

    public void handleJ(String content) {

    }

    public void handleK(String content) {

    }

    public void handleL(String content) {

    }

    public void handleM(String content) {

    }

    public void handleN(String content) {

    }

    public void handleO(String content) {

    }

    public void handleP(String content) {

    }

    public void handleQ(String content) {

    }

    public void handleR(String content) {

    }

    public void handleS(String content) {

    }

    public void handleT(String content) {

    }

    public void handleU(String content) {

    }

    public void handleV(String content) {

    }

    public void handleW(String content) {

    }

    public void handleX(String content) {

    }

    public void handleY(String content) {

    }

    public void handleZ(String content) {

    }

}
