package me.matl114.logitech;

public class TestPusher extends TestConsumer{
    public int maxCnt;
    public TestPusher(TestStack stack){
        super(stack);
        this.maxCnt=stack.getMaxAmount();
    }
    public TestPusher clone(){
        return (TestPusher)super.clone();
    }
    public void init(){
       // super.init();
        Tests.log("TestPusher empty init");
    }
    public void init(TestStack stack){
        super.init(stack);
        this.maxCnt=stack.getMaxAmount();
        Tests.log("TestPusher init");

    }
}
