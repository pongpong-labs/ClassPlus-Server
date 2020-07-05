package pnu.classplus.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@ApiModel(description="��ƼŬ ��")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Article {
	@ApiModelProperty(value="�Խñ� ��ȣ", dataType="Integer", required=true, example="1")
	private long id;
	@ApiModelProperty(value="��� ��¥", example="2019-08-20 12:12:14")
	private String regDate;
	@ApiModelProperty(value="�Խñ� ����", example="��������11")
	private String title;
	@ApiModelProperty(value="����", example="�����̾�.")
	private String body;
}
