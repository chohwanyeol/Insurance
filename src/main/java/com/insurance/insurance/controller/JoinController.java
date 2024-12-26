package com.insurance.insurance.controller;

import com.insurance.insurance.dto.*;
import com.insurance.insurance.entity.*;
import com.insurance.insurance.exception.DataNotFoundException;
import com.insurance.insurance.service.InsuranceService;
import com.insurance.insurance.service.ProductService;
import com.insurance.insurance.service.UserService;
import com.insurance.insurance.util.ResponseUtil;
import lombok.RequiredArgsConstructor;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/join")
@RequiredArgsConstructor
public class JoinController {

    private final InsuranceService insuranceService; // 보험 관련 서비스
    private final UserService userService; // 사용자 관련 서비스
    private final ProductService productService;

    @PreAuthorize("isAuthenticated")
    @GetMapping("")
    public ResponseEntity<?> getProducts(@AuthenticationPrincipal UserDetails userDetails){
        List<Product> productList = productService.getAll();
        List<ProductDTO> productDTOList = productList.stream()
                .map(product -> {
                            ProductDTO productDTO = new ProductDTO();
                            productDTO.EntityToDTO(product);
                            return productDTO;
                        }
                ).toList();
        return ResponseEntity.ok(productDTOList);
    }

    @PreAuthorize("isAuthenticated")
    @GetMapping("/health")
    public ResponseEntity<?> getHealthProduct(@AuthenticationPrincipal UserDetails userDetails){
        String username = userDetails.getUsername(); // 인증된 사용자 이름 가져오기
        SiteUser siteUser = userService.getByUsername(username); // 사용자 정보 조회
        try{
            HealthInsurance healthInsurance = insuranceService.getHealthBySiteUserAndStatus(siteUser,"active"); // 건강보험 가입 여부 확인
            return ResponseUtil.createSuccessResponse("status",false);
        }catch (DataNotFoundException e){
            return ResponseUtil.createSuccessResponse("status",true); // 미가입 상태 반환
        }
    }

    @PreAuthorize("isAuthenticated")
    @PostMapping("/health")
    public ResponseEntity<?> postHealthProduct(@AuthenticationPrincipal UserDetails userDetails, @Valid @RequestBody HealthJoinDTO healthJoinDTO){
        // 비동기 처리 - 건강보험 가입 요청 처리
        /*   ai에게 가입 가능 판별 및 리스크 책정 후 메일로 결과 발송?  */
        String username = userDetails.getUsername(); // 인증된 사용자 이름 가져오기
        SiteUser siteUser = userService.getByUsername(username);
        HealthInsurance healthInsurance = insuranceService.createHealth(siteUser,healthJoinDTO);
        InsuranceDTO insuranceDTO = new InsuranceDTO();
        insuranceDTO.EntityToDTO(healthInsurance);
        return ResponseEntity.ok(insuranceDTO);
    }


    @PreAuthorize("isAuthenticated")
    @GetMapping("/auto")
    public ResponseEntity<?> getAutoProduct(@AuthenticationPrincipal UserDetails userDetails){
        return ResponseUtil.createSuccessResponse("status",true); // 미가입 상태 반환
    }

    @PreAuthorize("isAuthenticated")
    @PostMapping("/auto")
    public ResponseEntity<?> postAutoProduct(@AuthenticationPrincipal UserDetails userDetails, @Valid @RequestBody AutoJoinDTO autoJoinDTO){
        String username = userDetails.getUsername(); // 인증된 사용자 이름 가져오기
        SiteUser siteUser = userService.getByUsername(username);

        try {
            AutoInsurance autoInsurance = insuranceService.getAutoBySiteUserAndVehicleNumber(siteUser, autoJoinDTO.getVehicleNumber()); // 자동차보험 가입 여부 확인
            return ResponseUtil.createSuccessResponse("status", false);
        }catch (DataNotFoundException e){
            AutoInsurance autoInsurance = insuranceService.createAuto(siteUser,autoJoinDTO);
            InsuranceDTO insuranceDTO = new InsuranceDTO();
            insuranceDTO.EntityToDTO(autoInsurance);
            return ResponseEntity.ok(insuranceDTO);
        }
    }


    @PreAuthorize("isAuthenticated")
    @GetMapping("/fire")
    public ResponseEntity<?> getFireProduct(@AuthenticationPrincipal UserDetails userDetails){
        return ResponseUtil.createSuccessResponse("status",true);
    }


    @PreAuthorize("isAuthenticated")
    @PostMapping("/fire")
    public ResponseEntity<?> postFireProduct(@AuthenticationPrincipal UserDetails userDetails, @Valid @RequestBody FireJoinDTO fireJoinDTO){
        String username = userDetails.getUsername(); // 인증된 사용자 이름 가져오기
        SiteUser siteUser = userService.getByUsername(username);

        try{
            FireInsurance fireInsurance = insuranceService.getFireBySiteUserAndPropertyAddressAndBuildingType(siteUser,fireJoinDTO.getPropertyAddress(),fireJoinDTO.getBuildingType()); // 화재보험 가입 여부 확인
            return ResponseUtil.createSuccessResponse("status",false);
        }catch (DataNotFoundException e){
            FireInsurance fireInsurance = insuranceService.createFire(siteUser,fireJoinDTO);
            InsuranceDTO insuranceDTO = new InsuranceDTO();
            insuranceDTO.EntityToDTO(fireInsurance);
            return ResponseEntity.ok(insuranceDTO);
        }


    }


    @PreAuthorize("isAuthenticated")
    @PostMapping("/fire/{id}")
    public ResponseEntity<?> joinFireProduct(@PathVariable("id")int id, @AuthenticationPrincipal UserDetails userDetails){
        String username = userDetails.getUsername(); // 인증된 사용자 이름 가져오기
        SiteUser siteUser = userService.getByUsername(username);
        FireInsurance fireInsurance = insuranceService.getFireBySiteUserAndId(siteUser,id);
        FireInsuranceDTO fireInsuranceDTO = new FireInsuranceDTO();
        fireInsuranceDTO.EntityToDTO(fireInsurance);
        return ResponseEntity.ok(fireInsuranceDTO);
    }

    @PreAuthorize("isAuthenticated")
    @PostMapping("/auto/{id}")
    public ResponseEntity<?> joinAutoProduct(@PathVariable("id")int id, @AuthenticationPrincipal UserDetails userDetails){
        String username = userDetails.getUsername(); // 인증된 사용자 이름 가져오기
        SiteUser siteUser = userService.getByUsername(username);
        AutoInsurance autoInsurance = insuranceService.getAutoBySiteUserAndId(siteUser,id);
        AutoInsuranceDTO autoInsuranceDTO = new AutoInsuranceDTO();
        autoInsuranceDTO.EntityToDTO(autoInsurance);
        return ResponseEntity.ok(autoInsuranceDTO);
    }

    @PreAuthorize("isAuthenticated")
    @PostMapping("/health/{id}")
    public ResponseEntity<?> joinHealthProduct(@PathVariable("id")int id, @AuthenticationPrincipal UserDetails userDetails){
        String username = userDetails.getUsername(); // 인증된 사용자 이름 가져오기
        SiteUser siteUser = userService.getByUsername(username);
        HealthInsurance healthInsurance = insuranceService.getHealthBySiteUserAndId(siteUser,id);
        HealthInsuranceDTO healthInsuranceDTO = new HealthInsuranceDTO();
        healthInsuranceDTO.EntityToDTO(healthInsurance);
        return ResponseEntity.ok(healthInsuranceDTO);
    }

    @PreAuthorize("isAuthenticated")
    @PostMapping("/{id}/yes")
    public ResponseEntity<?> yesJoinProduct(@PathVariable("id")int id, @AuthenticationPrincipal UserDetails userDetails){
        // 비동기 처리 - 화재보험 가입 요청 처리
        /*   ai에게 가입 가능 판별 및 리스크 책정  */
        String username = userDetails.getUsername(); // 인증된 사용자 이름 가져오기
        SiteUser siteUser = userService.getByUsername(username);
        insuranceService.startInsurance(siteUser,id);
        return ResponseUtil.createSuccessResponse("message","ok");
    }

    @PreAuthorize("isAuthenticated")
    @PostMapping("/{id}/no")
    public ResponseEntity<?> noJoinProduct(@PathVariable("id")int id, @AuthenticationPrincipal UserDetails userDetails){
        // 비동기 처리 - 화재보험 가입 요청 처리
        /*   ai에게 가입 가능 판별 및 리스크 책정  */
        String username = userDetails.getUsername(); // 인증된 사용자 이름 가져오기
        SiteUser siteUser = userService.getByUsername(username);
        insuranceService.deleteInsurance(siteUser,id);
        return ResponseUtil.createSuccessResponse("message","ok");
    }

}
