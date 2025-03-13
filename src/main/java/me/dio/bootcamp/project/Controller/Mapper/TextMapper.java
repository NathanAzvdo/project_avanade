package me.dio.bootcamp.project.Controller.Mapper;

import lombok.experimental.UtilityClass;
import me.dio.bootcamp.project.Controller.Request.TextRequest;
import me.dio.bootcamp.project.Controller.Response.TextResponse;
import me.dio.bootcamp.project.entity.Text;

@UtilityClass
public class TextMapper {

    public static Text toText(TextRequest textRequest) {
        return Text.builder()
                .id(textRequest.id())
                .text(textRequest.text())
                .build();
    }

    public static TextResponse toTextResponse(Text text) {
        return TextResponse.builder()
                .id(text.getId())
                .text(text.getText())
                .textReduced(text.getTextReduced())
                .build();
    }
}
