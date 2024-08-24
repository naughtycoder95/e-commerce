package org.example.backend.controller.admin;

import lombok.extern.slf4j.Slf4j;
import org.example.backend.model.entity.admin.AdminCoupon;
import org.example.backend.service.admin.AdminCouponService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Slf4j
@RestController
@RequestMapping("api/admin")
public class AdminCouponController {

    @Autowired
    AdminCouponService adminCouponService;

    //    TODO: 전체 조회 함수 + 검색 + 페이징
    @GetMapping("/admin-coupon")
    public ResponseEntity<Object> findAll(
            @RequestParam(defaultValue = "") String cpName,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        try {
            Pageable pageable = PageRequest.of(page, size);

//            전체 조회 서비스 함수 실행
            Page<AdminCoupon> pageList
                    = adminCouponService.findAllByAdminCouponNameContaining(cpName, pageable);

//            vue 로 json 데이터를 전송 : jsp (model == Map(키,값))
            Map<String, Object> response = new HashMap<>();
            response.put("adminCoupon", pageList.getContent());      // 쿠폰배열
            response.put("currentPage", pageList.getNumber());       // 현재페이지 번호(x)
            response.put("totalItems", pageList.getTotalElements()); // 전체데이터개수
            response.put("totalPages", pageList.getTotalPages());    // 전체페이지수(x)

            if (pageList.isEmpty() == true) {
//                1) pageList 값이 없으면 : DB 테이블 없음 => NO_CONTENT(203)
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            } else {
                return new ResponseEntity<>(response, HttpStatus.OK);
            }

        } catch (Exception e) {
//            TODO: INTERNAL_SERVER_ERROR(500) : 벡엔드 서버 에러
//               아래 코드는 프론트로(웹브라우저) 신호를(500) 보냄
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    //    TODO: 상세조회
    @GetMapping("/admin-coupon/{cpId}")
    public ResponseEntity<Object> findById(
            @PathVariable long cpId
    ) {
        try {
            Optional<AdminCoupon> optionalAdminCoupon = adminCouponService.findById(cpId);

            if (optionalAdminCoupon.isEmpty() == true) {
//                데이터 없음(203)
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            } else {
//                데이터 있음(200)
                return new ResponseEntity<>(optionalAdminCoupon.get()
                        , HttpStatus.OK);
            }

        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    //    TODO: 저장 함수
    @PostMapping("/admin-coupon")
    public ResponseEntity<Object> create(
            @RequestBody AdminCoupon adminCoupon
    ) {
        try {
            AdminCoupon adminCoupon2 = adminCouponService.save(adminCoupon);

            return new ResponseEntity<>(adminCoupon2, HttpStatus.OK);

        } catch (Exception e) {
//            500 전송
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    //    TODO: 수정함수
    @PutMapping("/admin-coupon/{cpId}")
    public ResponseEntity<Object> update(
            @PathVariable long cpId,
            @RequestBody AdminCoupon adminCoupon
    ) {
        try {
            AdminCoupon adminCoupon2 = adminCouponService.save(adminCoupon);

            return new ResponseEntity<>(adminCoupon2, HttpStatus.OK);
        } catch (Exception e) {
//            DB 에러 (서버 에러) -> 500 신호(INTERNAL_SERVER_ERROR)
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    //     TODO: 삭제 함수
    @DeleteMapping("/admin-coupon/deletion/{cpId}")
    public ResponseEntity<Object> delete(
            @PathVariable long cpId
    ) {
        try {
            boolean success = adminCouponService.removeById(cpId);

            if (success == true) {
                return new ResponseEntity<>(HttpStatus.OK);
            } else {
                // 삭제 실행 : 0건 삭제(삭제할 데이터 없음)
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }

        } catch (Exception e) {
//            서버(DB) 에러
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
