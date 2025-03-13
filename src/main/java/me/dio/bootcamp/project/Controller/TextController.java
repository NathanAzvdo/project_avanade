package me.dio.bootcamp.project.Controller;

import me.dio.bootcamp.project.Controller.Mapper.TextMapper;
import me.dio.bootcamp.project.Controller.Request.TextRequest;
import me.dio.bootcamp.project.Controller.Response.TextResponse;
import me.dio.bootcamp.project.entity.Text;
import me.dio.bootcamp.project.service.TextService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/text")
public class TextController {

    private final TextService textService;

    public TextController(TextService textService) {
        this.textService = textService;
    }

    @PostMapping("/save")
    public TextResponse saveText(@RequestBody  TextRequest textRequest, @RequestParam(defaultValue = "2") int lines) {
        Text toText = TextMapper.toText(textRequest);
        Text savedText = textService.saveText(toText.getText(), lines);
        return TextMapper.toTextResponse(savedText);
    }

    @GetMapping("find/content")
    public List<TextResponse> findTextByContent(@RequestBody TextRequest textRequest) {
        Text toText = TextMapper.toText(textRequest);
        List<Text> texts = textService.findByContent(toText.getText());
        return texts.stream()
                .map(TextMapper::toTextResponse)
                .collect(Collectors.toList());
    }

}
