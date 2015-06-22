
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import moa.MOAObject;
import moa.core.InstancesHeader;
import moa.streams.InstanceStream;
import org.joda.time.DateTime;
import org.joda.time.LocalDateTime;
import org.joda.time.format.DateTimeFormat;
import weka.core.Attribute;
import weka.core.DenseInstance;
import weka.core.FastVector;
import weka.core.Instance;
import weka.core.Instances;
import weka.filters.Filter;
import weka.filters.unsupervised.attribute.StringToWordVector;
/**
 * Class generates a training stream from an array of tweets, so that the classifiers can be trained.
 */
public class ExtendedBayesianStream implements InstanceStream {

	/**
	 * trainingstream for the models - updated over time, instances are deleted
	 * after training
	 */
	public Instances trainingSet;

	/**
	 * saves temporary new training instances
	 */
	public Instances newInstances;

	/**
	 * contains all unfiltered training instances from the beginning
	 */
	public Instances unfilteredTrainingSet;

	/**
	 * number of training instances left in the training stream
	 */
	int numInstances;

	/**
	 * Filter for the vector tf*idf representation of the instances
	 * 
	 * @param tweets
	 *            - training tweets for the stream
	 */
	StringToWordVector vector;

	public ExtendedBayesianStream(ArrayList<ExtendedTweet> tweets) throws Exception {

		// initialising the trainingSet

		FastVector atts = new FastVector(2);
        FastVector fvClassVal = new FastVector(2);
		//FastVector atts = new FastVector(3);
		//FastVector fvClassVal = new FastVector(3);

		//fvClassVal.addElement("H");
		//fvClassVal.addElement("S");
        fvClassVal.addElement("4");
        fvClassVal.addElement("0");

		Attribute ClassAttribute = new Attribute("sentimentClass", fvClassVal);

		atts.addElement(ClassAttribute);
		atts.addElement(new Attribute("tweet", (FastVector) null));
//		System.out.println("atts vector length: "+ atts.size());
		//atts.addElement(new Attribute("date", (FastVector) null));
//		System.out.println("atts vector length: "+ atts.size());
//		System.out.println("Dada: " + atts.elementAt(2));
		unfilteredTrainingSet = new Instances("TrainigSet", atts, 0);
//		System.out.println("Kaboom " + unfilteredTrainingSet.attribute(2));
		unfilteredTrainingSet.setClassIndex(0);

		int i = 0;
        boolean firstTimestampSet = false;
        int firstTimestamp = 0; //in our case, the first week we look at
		while (i < tweets.size()) {
//			System.out.println("Debugging like a pro.");
			numInstances++;
			ExtendedTweet tweet = tweets.get(tweets.size() - 1); //!!!!
			tweets.remove(tweets.size() - 1);
			Instance inst = new DenseInstance(2);
			//Instance inst = new DenseInstance(3);
			inst.setValue(unfilteredTrainingSet.attribute(0), tweet.getType());
			inst.setValue(unfilteredTrainingSet.attribute(1), tweet.getText());
			
//			System.out.println("What's the Tweet's date?: " + tweet.getDate());
			/*
			 * attenzione
			 */
			String dateString = tweet.getDate();
		    //DateFormat dateFormat = new SimpleDateFormat("yyyy-mm-dd hh:mm:ss");
            String parseString = "YYYY-MM-dd HH:mm:ss.0";
            LocalDateTime joda = LocalDateTime.parse(dateString, DateTimeFormat.forPattern(parseString));
            //DateFormat dateFormat = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", Locale.US);
            //DateFormat dateFormat = new SimpleDateFormat(parseString, Locale.US);
		    //Date date = dateFormat.parse(dateString ); //Hier passiert der Fehler!
            //System.out.println("Date String (original) from ExtendedDatabaseConnection: " + dateString);
            //System.out.println("Date in Bayesian Stream: " + date);
            //DateTime joda = new DateTime(date);
            //System.out.println("Joda Date in Bayesian Stream: " + joda);
            int week = joda.getWeekOfWeekyear();
            int day = joda.getDayOfYear();
            int hour = joda.getHourOfDay();
            int hourOfYear = hour + 24 * day;
            //---Testing! Setting to day of year
            //int week = joda.getDayOfYear();
            //System.out.println("Week Of Weekyear in Bayesian Stream: " + week);
            //System.out.println("Day of Year in Bayesian Stream: " + day);
            if(firstTimestampSet == false)
            {
                firstTimestamp = week;
                firstTimestampSet = true;
            }
		    //long unixTime = (long) date.getTime()/1000;
//		    System.out.println("Unix Time: " + unixTime );
            //double unixFloat = unixTime;
            //System.out.println("Flot time: " + unixFloat);
            inst.setWeight(day);
            //inst.setWeight(week);
            //inst.setWeight(hourOfYear);
            //System.out.println("Instance weight: " + inst.weight());
//		    System.out.println(inst);
//			double instanceValue = inst.value(unfilteredTrainingSet.attribute(1));
			
			/*System.out.println("This instances value attribute 0 " + inst.value(unfilteredTrainingSet.attribute(0)));
			System.out.println("This instances value attribute 1 " + inst.value(unfilteredTrainingSet.attribute(1)));
			System.out.println("This instances value attribute 2 " + inst.value(unfilteredTrainingSet.attribute(2)));
			System.out.println("This instance "+ inst);
			System.out.println(inst.toString());
			System.out.println(inst.toString(2));
			System.out.println(inst.toString(ClassAttribute));
			System.out.println(inst.toString(unfilteredTrainingSet.attribute(2)));*/
			unfilteredTrainingSet.add(inst);

		}
//		System.out.println("Finished the loop of all tweets");
		vector = new StringToWordVector();
		//attenzione here please
		String[] options = new String[2];
		//String[] options = new String[3];
		options[0] = "-C";
		options[1] = "-I";


		vector.setOptions(options);
		vector.setInputFormat(unfilteredTrainingSet);
		// vector.setOutputWordCounts(true);
		// vector.setIDFTransform(true);
		// vector.setNormalizeDocLength();

		trainingSet = Filter.useFilter(unfilteredTrainingSet, vector);
		//System.out.println("This point is never reached");

	}

	@Override
	public int measureByteSize() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public MOAObject copy() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void getDescription(StringBuilder sb, int indent) {
		// TODO Auto-generated method stub

	}

	@Override
	public InstancesHeader getHeader() {

		return null;
	}

	@Override
	public long estimatedRemainingInstances() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public boolean hasMoreInstances() {
		if (numInstances > 0) {
			return true;
		}
		return false;
	}

	/**
	 * Method returns a training instance and deletes it from the training
	 * stream
	 */
	public Instance nextInstance() {
		/*
		 * Extended to include timestamps
		 * Use weight as code for unix timetamp
		 */
		if (this.hasMoreInstances()) {
			int moin = trainingSet.size() - 1;
			Instance inst = trainingSet.get(moin);
			//inst.setValue(2, "2014-test");
			trainingSet.remove(moin);
			numInstances--;
			return inst;
		}
		return null;
	}

	@Override
	public boolean isRestartable() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void restart() {
		// TODO Auto-generated method stub

	}

	public StringToWordVector getVector() {
		return this.vector;
	}

	public Instances getInstances() {
		return this.trainingSet;
	}

	/**
	 * Method adds a new instance to the training stream
	 * 
	 * @param t
	 *            - new training instance
	 */
	public void addInstanceToStream(ExtendedTweet t) {

		Instance inst = new DenseInstance(2);
		
		//Once for unfiltered training set, once for new Instances

		inst.setValue(unfilteredTrainingSet.attribute(0), t.getType());

		inst.setValue(unfilteredTrainingSet.attribute(1), t.getText());
		
		//inst.setValue(unfilteredTrainingSet.attribute(2), t.getDate());

		unfilteredTrainingSet.add(inst);

		Instance inst2 = new DenseInstance(2);

		inst2.setValue(newInstances.attribute(0), t.getType());

		inst2.setValue(newInstances.attribute(1), t.getText());
		
		//inst2.setValue(newInstances.attribute(2), t.getDate());

		newInstances.add(inst2);

	}

	/**
	 * Method generates a new temporary dataset for storage of new training tweets
	 */
	public void createNewUpdateStream() {
		// initialising the trainingSet

		FastVector atts = new FastVector(2);
		FastVector fvClassVal = new FastVector(2);

		//fvClassVal.addElement("H");
		//fvClassVal.addElement("S");
        fvClassVal.addElement("4");
        fvClassVal.addElement("0");
		// fvClassVal.addElement("neutral");

		Attribute ClassAttribute = new Attribute("sentimentClass", fvClassVal);

		// atts.addElement(new Attribute("tweet", (FastVector) null));
		atts.addElement(ClassAttribute);
		atts.addElement(new Attribute("tweet", (FastVector) null));
		newInstances = new Instances("TrainigSet", atts, 0);
		newInstances.setClassIndex(0);

	}

	/**
	 * Method updates the training stream with new instances
	 * @throws Exception
	 */
	public void updateTrainingStream() throws Exception {
		this.trainingSet = Filter.useFilter(newInstances, vector);
		numInstances = newInstances.size();
	}

}
