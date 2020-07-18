package pnu.classplus.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@ApiModel(description="아티클 모델")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Article {
	@ApiModelProperty(value="게시글 번호", dataType="Integer", required=true, example="1")
	private long id;
	@ApiModelProperty(value="등록 날짜", example="2019-08-20 12:12:14")
	private String regDate;
	@ApiModelProperty(value="게시글 제목", example="제목제목11")
	private String title;
	@ApiModelProperty(value="내용", example="내용이야.")
	private String body;
}