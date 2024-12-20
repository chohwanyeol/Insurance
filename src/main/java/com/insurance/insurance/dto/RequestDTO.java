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
    private Integer requestID;
    private String productName;
    private String type;        //요청타입
    private String content;     //요청내용
    private Integer price;      //비용
    private LocalDateTime request_date;     //요청일
    private LocalDateTime payment_date;     //지급일
    private String status;      //상태
    private String description;     //설명

    public void EntityToDTO(Request request){
        this.requestID = request.getId();
        this.productName = request.getInsurance().getProduct().getName();
        this.type = request.getType();
        this.content = request.getContent();
        this.price = request.getPrice();
        this.request_date = request.getRequest_date();
        this.payment_date = request.getPayment_date();
        this.status = Status.getKoreanStatus(request.getStatus());
        this.description = request.getDescription();
    }
}
