package com.my.app.appgodo.Dto;

/**
 * Created by Mark on 2015-12-21.
 */
public class HttpPostDto {
    private String hkey;
    private String hvalue;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof HttpPostDto)) return false;

        HttpPostDto HttpPostDto = (HttpPostDto) o;

        if (getHkey() != null ? !getHkey().equals(HttpPostDto.getHkey()) : HttpPostDto.getHkey() != null)
            return false;
        return !(getHvalue() != null ? !getHvalue().equals(HttpPostDto.getHvalue()) : HttpPostDto.getHvalue() != null);

    }

    @Override
    public int hashCode() {
        int result = getHkey() != null ? getHkey().hashCode() : 0;
        result = 31 * result + (getHvalue() != null ? getHvalue().hashCode() : 0);
        return result;
    }

    public String getHkey() {
        return hkey;
    }

    public void setHkey(String hkey) {
        this.hkey = hkey;
    }

    public String getHvalue() {
        return hvalue;
    }

    public void setHvalue(String hvalue) {
        this.hvalue = hvalue;
    }
}
