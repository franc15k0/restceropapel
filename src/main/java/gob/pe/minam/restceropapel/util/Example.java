package gob.pe.minam.restceropapel.util;

import lombok.*;

import java.sql.Timestamp;
@Builder
@Getter
@Setter
@AllArgsConstructor
public class Example {
    public Example(){
        campo3 = "Nombre:" + campo1 + "- Edad:" + campo2;
    }
    private String campo1;
    private String campo2;
    private String campo3;
    public String toString() {
        return campo3;
    }
/*    public static void main(String[] args) {

        *//*System.out.println( new Timestamp(System.currentTimeMillis()));*//*
        String string = "004_03455_642";
        String[] parts = string.split("_", 2);
        String part1 = parts[0]; // 004
        String part2 = parts[1]; // 034556-4
        System.out.println(part1);
        System.out.println(part2);
    }*/
}
