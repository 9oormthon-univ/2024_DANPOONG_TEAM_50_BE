package com.example.mymoo.domain.child.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "TB_Location")
@Entity
public class Location {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "do", nullable = false, columnDefinition = "char(10)")
    private String Do;

    @Column(name="sigun", nullable = false, columnDefinition = "char(10)")
    private String sigun;

    @Column(name="gu",  columnDefinition = "char(10)")
    private String gu;

    @Column(name="dong", columnDefinition = "char(100)")
    private String dongUepmean;

    @Column(name="ro", nullable = false, columnDefinition = "char(50)")
    private String ro;

    @Column(name="detail", nullable = false, columnDefinition = "char(100)")
    private String detail;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "child_id", nullable = false)
    private Child child;

    @Builder
    public Location(
            final String Do,
            final String sigun,
            final String gu,
            final String dongUepmean,
            final String ro,
            final String detail,
            final Child child
    ){
        this.Do = Do;
        this.sigun = sigun;
        this.gu = gu;
        this.dongUepmean = dongUepmean;
        this.ro = ro;
        this.detail = detail;
        this.child = child;
    }
}
