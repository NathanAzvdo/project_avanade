package me.dio.bootcamp.project.Controller;

import me.dio.bootcamp.project.Controller.Mapper.TextMapper;
import me.dio.bootcamp.project.Controller.Request.TextRequest;
import me.dio.bootcamp.project.Controller.Response.TextResponse;
import me.dio.bootcamp.project.entity.Text;
import me.dio.bootcamp.project.service.TextService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/text")
public class TextController {

    private final TextService textService;

    public TextController(TextService textService) {
        this.textService = textService;
    }

    @PostMapping("/save")
    public TextResponse saveText( @RequestBody  TextRequest textRequest){
        Text toText = TextMapper.toText(textRequest);
        Text savedText = textService.saveText(toText.getText());
        return TextMapper.toTextResponse(savedText);
    }


}
