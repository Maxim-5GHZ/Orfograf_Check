package com.example.errorfreetext.service.client;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SpellerError {

    
    private int pos;

    
    private int len;

    
    private String word;

    // варианты исправлений
    private List<String> s;
}
