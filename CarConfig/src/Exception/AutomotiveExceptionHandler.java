package Exception;

public class AutomotiveExceptionHandler extends ExceptionHanlderAbs {

	private static final long serialVersionUID = 6757782982647322711L;

	final public int OPTION_NOT_DEFINED = 1;
	final public int OPTION_NOT_SET_YET = 2;
	final public int OPTION_SET_NOT_DEFINED = 3;
	final public int MAKE_NOT_DEFINED = 4;
	final public int MODEL_NOT_DEFINED = 5;

	@Override
	public void raiseException(int exceptionNumber) {
		switch (exceptionNumber) {
		case (OPTION_NOT_DEFINED):
			System.err.println("option is not defined");
			break;
		case (OPTION_NOT_SET_YET):
			System.err.println("option is not selected yet");
			break;
		case (OPTION_SET_NOT_DEFINED):
			System.err.println("option set is not defined");
			break;
		case (MAKE_NOT_DEFINED):
			System.err.println("make is not defined");
			break;
		case (MODEL_NOT_DEFINED):
			System.err.println("model is not defiend");
			break;
		}

	}

	@Override
	public void fixException(int exceptionNumber) {
		switch (exceptionNumber) {
		case (OPTION_NOT_DEFINED):
			System.out.println("a default option is now created");
			break;
		case (OPTION_NOT_SET_YET):
			System.out.println("an empty option is selected");
			break;
		case (OPTION_SET_NOT_DEFINED):
			System.out.println("a default option set is now created");
			break;
		case (MAKE_NOT_DEFINED):
			System.out.println("make is now set to default make");
			break;
		case (MODEL_NOT_DEFINED):
			System.out.println("model is now set to default model");
			break;
		}

	}

}
