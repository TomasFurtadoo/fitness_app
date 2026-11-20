package com.tomas.nutrition.exception;

import java.time.Instant;
import java.util.List;

public record ErrorResponse(
        Instant timestamp,
        int status,
        String erro,
        String mensagem,
        List<String> detalhes
) {
    public static ErrorResponse of(int status, String erro, String mensagem) {
        return new ErrorResponse(Instant.now(), status, erro, mensagem, List.of());
    }

    public static ErrorResponse of(int status, String erro, String mensagem, List<String> detalhes) {
        return new ErrorResponse(Instant.now(), status, erro, mensagem, detalhes);
    }
}