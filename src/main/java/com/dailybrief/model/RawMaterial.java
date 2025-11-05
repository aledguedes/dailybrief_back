package com.dailybrief.model;

import java.time.ZonedDateTime;
import lombok.Data;
import jakarta.persistence.*;

@Entity
@Data
@Table(name = "tbl_raw_materials")
public class RawMaterial {

	@Id
	@Column(name = "id", length = 255)
	private String id;

	@Column(name = "user_id", nullable = false, length = 255)
	private String userId;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "task_id", nullable = false)
	private Material material;

//	@Lob
	@Column(name = "url", nullable = false)
	private String url;

//    @Lob
	@Column(name = "content", nullable = false)
	private String content;

	@Column(name = "created_at", nullable = false)
	private ZonedDateTime createdAt;
}
