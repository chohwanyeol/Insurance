package com.insurance.insurance.dto;


import com.insurance.insurance.entity.Request;
import com.insurance.insurance.enums.Status;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.swing.text.html.parser.Entity;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class RequestDTO {
    private Integer requestId;
    private String productTitle;
    private String type;        //요청타입
    private String content;     //요청내용
    private Integer price;      //비용
    private LocalDateTime requestDate;     //요청일
    private String status;      //상태
    private String description;     //설명
    private Integer transactionId;

    public void EntityToDTO(Request request){
        this.requestId = request.getId();
        this.productTitle = request.getInsurance().getProduct().getTitle();
        this.type = request.getType();
        this.content = request.getContent();
        this.price = request.getPrice();
        this.requestDate = request.getRequestDate();
        this.status = Status.getKoreanStatus(request.getStatus());
        this.description = request.getDescription();
    }
}
