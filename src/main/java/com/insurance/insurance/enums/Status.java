package com.insurance.insurance.enums;


import lombok.Getter;

@Getter
public enum Status {
    PENDING("대기 중"),
    APPROVED("승인됨"),
    REJECTED("거부됨");

    private final String korean;

    Status(String korean) {
        this.korean = korean;
    }

    public String getKorean() {
        return korean;
    }

    public static String getKoreanStatus(String status) {
        try {
            return Status.valueOf(status.toUpperCase()).getKorean();
        } catch (IllegalArgumentException e) {
            return "알 수 없음";
        }
    }
}
