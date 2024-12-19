package me.matl114.logitech;

public class TestConsumer extends TestCounter{
    public TestConsumer(TestStack stack) {
        super(stack);
    }
    @Override
	public TestConsumer clone(){
        return (TestConsumer) super.clone();
    }
}
