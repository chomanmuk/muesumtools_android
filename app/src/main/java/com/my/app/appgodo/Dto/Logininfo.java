package com.my.app.appgodo.Dto;

/**
 * Created by Mark on 2015-12-21.
 */
public class Logininfo {
    private Long m_no;
    private String m_id;
    private String m_name;

    public String getM_name() {
        return m_name;
    }

    public void setM_name(String m_name) {
        this.m_name = m_name;
    }

    public Long getM_no() {
        return m_no;
    }

    public void setM_no(Long m_no) {
        this.m_no = m_no;
    }

    public String getM_id() {
        return m_id;
    }

    public void setM_id(String m_id) {
        this.m_id = m_id;
    }

    @Override
    public String toString() {
        return "Logininfo{" +
                "m_no=" + m_no +
                ", m_id='" + m_id + '\'' +
                ", m_name='" + m_name + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Logininfo)) return false;

        Logininfo logininfo = (Logininfo) o;

        if (getM_no() != null ? !getM_no().equals(logininfo.getM_no()) : logininfo.getM_no() != null)
            return false;
        if (getM_id() != null ? !getM_id().equals(logininfo.getM_id()) : logininfo.getM_id() != null)
            return false;
        return !(getM_name() != null ? !getM_name().equals(logininfo.getM_name()) : logininfo.getM_name() != null);

    }

    @Override
    public int hashCode() {
        int result = getM_no() != null ? getM_no().hashCode() : 0;
        result = 31 * result + (getM_id() != null ? getM_id().hashCode() : 0);
        result = 31 * result + (getM_name() != null ? getM_name().hashCode() : 0);
        return result;
    }
}
