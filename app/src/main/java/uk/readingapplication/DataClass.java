package uk.readingapplication;



// class that retrieves all information in string formats
public class DataClass {

    private String dataTitle;
    private String dataDesc;
    private String dataLang;

    private String dataStory;
    private String dataImage;

    private String dataAudio;

    private String dataVideo;
    private String key;




    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }




    public String getDataTitle() {

        return dataTitle;
    }

    public String getDataDesc() {

        return dataDesc;
    }

    public String getDataLang() {

        return dataLang;
    }

    public String getDataStory() {

        return dataStory;
    }

    public String getDataImage() {

        return dataImage;
    }

    public String getDataVideo(){

        return dataVideo;
    }

    public String getDataAudio() {

        return dataAudio;

    }

    public DataClass(String dataTitle, String dataDesc, String dataLang, String dataStory, String dataImage, String dataVideo, String dataAudio) {
        this.dataTitle = dataTitle;
        this.dataDesc = dataDesc;
        this.dataLang = dataLang;
        this.dataStory = dataStory;
        this.dataImage = dataImage;
        this.dataVideo = dataVideo;
        this.dataAudio = dataAudio;


    }

    public DataClass(){


    }


}
