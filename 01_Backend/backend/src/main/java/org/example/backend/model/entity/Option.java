package org.example.backend.model.entity;

import jakarta.persistence.*;
import lombok.*;
import org.example.backend.model.common.BaseTimeEntity2;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

/**
 * packageName : org.example.backend.model.entity
 * fileName : Option
 * author : kimtaewan
 * date : 2024-05-01
 * description :
 * 요약 :
 * <p>
 * ===========================================================
 * DATE            AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2024-05-01         kimtaewan          최초 생성
 */
@Entity
@Table(name="TB_OPTION")
@SequenceGenerator(
        name = "SEQ_TB_OPTION_OP_ID_GENERATOR"
        , sequenceName = "SEQ_TB_OPTION_OP_ID"
        , allocationSize = 1
)
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@DynamicInsert
@DynamicUpdate
@Where(clause = "STATUS = 'Y'")
@SQLDelete(sql = "UPDATE TB_OPTION SET STATUS = 'N', DEL_DATE = TO_CHAR(SYSDATE, 'YYYY-MM-DD HH24:MI:SS') WHERE OP_ID = ?")
public class Option extends BaseTimeEntity2 {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE
                    , generator = "SEQ_TB_OPTION_OP_ID_GENERATOR")
    private Long opId;              // 옵션 ID (PK)
    private Long pdId;              // 상품 ID
    private String opName;          // 옵션명
    private Integer opPrice;        // 추가금액
    private Integer opStock;        // 재고
    private String opCode;          // 옵션분류코드
}
