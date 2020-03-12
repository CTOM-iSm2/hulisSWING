package controller.globalCommands;


/**
 * This interface describes a command
 * 
 * @author ngoudard
 *
 */
public interface ICommand {
	/**
	 * execute the command
	 * @throws Exception 
	 */
	public void execute() throws Exception;
	
	/**
	 * 
	 * @return
	 */
	public boolean isSucceed() ;
	
	/**
	 * 
	 * @return
	 */
	public String getFailReason();

}
