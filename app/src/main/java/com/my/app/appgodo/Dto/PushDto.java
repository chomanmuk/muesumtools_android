package com.my.app.appgodo.Dto;

/**
 * Created by Mark on 2015-12-23.
 */
public class PushDto {
    private String ordno;
    private Long ptype;
    private String message ="";
    private String regdt ="";

    public String getOrdno() {
        return ordno;
    }

    public void setOrdno(String ordno) {
        this.ordno = ordno;
    }

    @Override
    public String toString() {
        return "PushDto{" +
                "ordno='" + ordno + '\'' +
                ", ptype=" + ptype +
                ", message='" + message + '\'' +
                ", regdt='" + regdt + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PushDto)) return false;

        PushDto pushDto = (PushDto) o;

        if (getOrdno() != null ? !getOrdno().equals(pushDto.getOrdno()) : pushDto.getOrdno() != null)
            return false;
        if (getPtype() != null ? !getPtype().equals(pushDto.getPtype()) : pushDto.getPtype() != null)
            return false;
        if (getMessage() != null ? !getMessage().equals(pushDto.getMessage()) : pushDto.getMessage() != null)
            return false;
        return !(getRegdt() != null ? !getRegdt().equals(pushDto.getRegdt()) : pushDto.getRegdt() != null);

    }

    @Override
    public int hashCode() {
        int result = getOrdno() != null ? getOrdno().hashCode() : 0;
        result = 31 * result + (getPtype() != null ? getPtype().hashCode() : 0);
        result = 31 * result + (getMessage() != null ? getMessage().hashCode() : 0);
        result = 31 * result + (getRegdt() != null ? getRegdt().hashCode() : 0);
        return result;
    }


    public Long getPtype() {
        return ptype;
    }

    public void setPtype(Long ptype) {
        this.ptype = ptype;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getRegdt() {
        return regdt;
    }

    public void setRegdt(String regdt) {
        this.regdt = regdt;
    }

}
