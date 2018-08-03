package com.xull.web.shiro.jcaptcha;

import com.octo.captcha.component.image.backgroundgenerator.BackgroundGenerator;
import com.octo.captcha.component.image.backgroundgenerator.UniColorBackgroundGenerator;
import com.octo.captcha.component.image.color.RandomRangeColorGenerator;
import com.octo.captcha.component.image.deformation.ImageDeformation;
import com.octo.captcha.component.image.deformation.ImageDeformationByFilters;
import com.octo.captcha.component.image.fontgenerator.FontGenerator;
import com.octo.captcha.component.image.fontgenerator.RandomFontGenerator;
import com.octo.captcha.component.image.textpaster.DecoratedRandomTextPaster;
import com.octo.captcha.component.image.textpaster.TextPaster;
import com.octo.captcha.component.image.textpaster.textdecorator.BaffleTextDecorator;
import com.octo.captcha.component.image.textpaster.textdecorator.LineTextDecorator;
import com.octo.captcha.component.image.textpaster.textdecorator.TextDecorator;
import com.octo.captcha.component.image.wordtoimage.DeformedComposedWordToImage;
import com.octo.captcha.component.image.wordtoimage.WordToImage;
import com.octo.captcha.component.word.FileDictionary;
import com.octo.captcha.component.word.wordgenerator.ComposeDictionaryWordGenerator;
import com.octo.captcha.component.word.wordgenerator.WordGenerator;
import com.octo.captcha.engine.image.ListImageCaptchaEngine;
import com.octo.captcha.image.gimpy.GimpyFactory;

import java.awt.*;
import java.awt.image.ImageFilter;

/**
 * JCaptcha验证码图片生成引擎
 * @author ur0638
 */
public class GMailEngine extends ListImageCaptchaEngine {

    @Override
    protected void buildInitialFactories() {

        int minWordLength = 4;
        int maxWordLength = 4;
        int minFontSize = 20;
        int maxFontSize = 23;
        int imageWidth = 100;
        int imageHeight = 34;
        //随机文本的字典，这里是使用jcaptcha-1.0.jar中的文本字典，字典名称为toddlist.properties
        WordGenerator wordGenerator = new ComposeDictionaryWordGenerator(new FileDictionary("toddlist"));
        //干扰线随机范围颜色
        RandomRangeColorGenerator lineColors = new RandomRangeColorGenerator(
                new int[] { 100, 255 }, new int[] { 100, 255 }, new int[] { 100, 255},new int[]{10,200});
        RandomRangeColorGenerator baffleColors = new RandomRangeColorGenerator(
                new int[] { 150, 180 }, new int[] { 150, 180 }, new int[] { 150, 180},new int[]{120,200});
//new LineTextDecorator(new Integer(1),lineColors,2)
        //随机文字多少和颜色，参数1和2表示最少生成多少个文字和最多生成多少个
        TextPaster randomPaster = new DecoratedRandomTextPaster(
                minWordLength,
                maxWordLength,
                //生成随机颜色，参数分别表示RGBA的取值范围
                new RandomRangeColorGenerator(
                        new int[]{0,150},
                        new int[]{0,255},
                        new int[]{0,255},
                        new int[]{255,255}),
                new TextDecorator[]{
                        new LineTextDecorator(new Integer(1),lineColors),
                        new BaffleTextDecorator(new Integer(4),baffleColors)});
        //生成背景的大小,也可以设置背景颜色和随机背景颜色
        BackgroundGenerator background = new UniColorBackgroundGenerator(
                imageWidth, imageHeight
        );
        //随机生成的字体大小和字体类型，参数1和2表示最小和最大的字体大小，第三个表示随机的字体
        FontGenerator fontGenerator = new RandomFontGenerator(minFontSize, maxFontSize,
                new Font[]{new Font("nyala", Font.BOLD, maxFontSize),
                        new Font("Bell MT", Font.PLAIN, maxFontSize),
                        new Font("Credit valley", Font.BOLD, maxFontSize),
                        new Font("Arial", Font.BOLD, maxFontSize),
                        new Font("Tahoma",Font.PLAIN,maxFontSize)});


        ImageDeformation postDef = new ImageDeformationByFilters(
                new ImageFilter[]{}
        );
        ImageDeformation backDef = new ImageDeformationByFilters(
                new ImageFilter[]{}
        );
        ImageDeformation textDef = new ImageDeformationByFilters(
                new ImageFilter[]{}
        );
        //结合上面的对象构件一个从文本生成图片的对象
        WordToImage wordToImage = new DeformedComposedWordToImage(fontGenerator,
                background, randomPaster, backDef, textDef, postDef);
        addFactory(new GimpyFactory(wordGenerator, wordToImage));
    }

}
