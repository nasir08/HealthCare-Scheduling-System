package data;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;

public class Progress {

	private DoubleProperty progressCounter;

	public double getProgress() 
	{
		if(progressCounter != null)
		{
			return progressCounter.get();
		}
		else return 0;
	}

	public final void setProgress(double number) {
		this.numberProperty().set(number);
	}
	
	public final DoubleProperty numberProperty()
	{
		if(progressCounter == null)
		{
			progressCounter = new SimpleDoubleProperty();
		}
		return progressCounter;
	}
}
