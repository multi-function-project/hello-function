package com.example.hello.model;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class HelloOutput implements Serializable {
    private static final long serialVersionUID = 1L;
    private String message;
}
