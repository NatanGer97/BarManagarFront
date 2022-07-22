package com.example.barmanagarfront.factories;

import com.example.barmanagarfront.Singeltones.SeatsManager;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.server.StreamResource;

public final class ImageFactory
{
    private static ImageFactory instance;
    private  Image image;
    private  String  imgUri;

    private ImageFactory()
    {

    }
    public static ImageFactory getInstance()
    {
//        ImageFactory imageFactory = instance;
        if ( instance != null ){
            return instance;
        }

        synchronized(SeatsManager.class)
        {
            if ( instance == null )
            {
                instance = new ImageFactory();
            }
            return  instance;
        }
    }

    public   Image getImage(String imgName)
    {
        image = null;
        StreamResource streamResource = new StreamResource("",() ->
                this.getClass().getResourceAsStream(String.format("/images/%s",imgName)));
        image = new Image(streamResource,"");

        return image;

    }

    /*StreamResource imageResourceForAnimation = new StreamResource("",
            () -> getClass().getResourceAsStream("/images/bar_logo.gif"));
    splashImage = new Image(imageResourceForAnimation, "animation");*/
}
