package com.wuguangxin.dao;

/**
 * 测试数据库操作的Bean
 */
public class TestBean {
    private int _id; // id
    private String uno; // 银行卡号
    private String bno; // 目标银行卡号
    private String bnum; // 转账金额

    public TestBean() {
    }

    public TestBean(String uno, String bno, String bnum) {
        this._id = _id;
        this.uno = uno;
        this.bno = bno;
        this.bnum = bnum;
    }
    public TestBean(int _id, String uno, String bno, String bnum) {
        this._id = _id;
        this.uno = uno;
        this.bno = bno;
        this.bnum = bnum;
    }

    public int get_id() {
        return _id;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    public String getUno() {
        return uno;
    }

    public void setUno(String uno) {
        this.uno = uno;
    }

    public String getBno() {
        return bno;
    }

    public void setBno(String bno) {
        this.bno = bno;
    }

    public String getBnum() {
        return bnum;
    }

    public void setBnum(String bnum) {
        this.bnum = bnum;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Transinfo{");
        sb.append("_id=").append(_id);
        sb.append(", uno='").append(uno).append('\'');
        sb.append(", bno='").append(bno).append('\'');
        sb.append(", bnum='").append(bnum).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
