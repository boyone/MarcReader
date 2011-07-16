package th.co.opendream.opendata;

import java.util.List;
import java.util.Map;

import org.pentaho.di.core.CheckResultInterface;
import org.pentaho.di.core.Counter;
import org.pentaho.di.core.database.DatabaseMeta;
import org.pentaho.di.core.exception.KettleException;
import org.pentaho.di.core.exception.KettleStepException;
import org.pentaho.di.core.exception.KettleXMLException;
import org.pentaho.di.core.row.RowMetaInterface;
import org.pentaho.di.core.row.ValueMeta;
import org.pentaho.di.core.row.ValueMetaInterface;
import org.pentaho.di.core.variables.VariableSpace;
import org.pentaho.di.core.xml.XMLHandler;
import org.pentaho.di.i18n.BaseMessages;
import org.pentaho.di.repository.ObjectId;
import org.pentaho.di.repository.Repository;
import org.pentaho.di.trans.Trans;
import org.pentaho.di.trans.TransMeta;
import org.pentaho.di.trans.step.BaseStepMeta;
import org.pentaho.di.trans.step.StepDataInterface;
import org.pentaho.di.trans.step.StepInterface;
import org.pentaho.di.trans.step.StepMeta;
import org.pentaho.di.trans.step.StepMetaInterface;
import org.w3c.dom.Node;

public class MarcReaderMeta extends BaseStepMeta implements StepMetaInterface {
	
	private static Class<?> PKG = MarcReaderMeta.class; // for i18n purposes
	private String fileName;
	private String jsonFieldName;
	private String encoding;
	
	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getJsonFieldName() {
		return jsonFieldName;
	}

	public void setJsonFieldName(String jsonFieldName) {
		this.jsonFieldName = jsonFieldName;
	}

	public String getEncoding() {
		return encoding;
	}

	public void setEncoding(String encoding) {
		this.encoding = encoding;
	}

	@Override
	public void setDefault() {
		jsonFieldName = "json";		
	}
	
	@Override
	public void loadXML(Node stepnode, List<DatabaseMeta> databases,
			Map<String, Counter> counters) throws KettleXMLException {
		readData(stepnode);
	}
	
	private void readData(Node stepnode) throws KettleXMLException {
		try	{
			fileName = XMLHandler.getTagValue(stepnode, "file_name"); //$NON-NLS-1$
			encoding  = XMLHandler.getTagValue(stepnode, "encoding"); //$NON-NLS-1$
			jsonFieldName = XMLHandler.getTagValue(stepnode, "json_field_name"); //$NON-NLS-1$
		}catch(Exception e)	{
			throw new KettleXMLException(BaseMessages.getString(PKG, "MarcReaderMeta.Exception.UnableToLoadStepInfo"), e); //$NON-NLS-1$
		}
	}
	
	public Object clone(){
		MarcReaderMeta retval = (MarcReaderMeta)super.clone();
		return retval;
	}
	
	@Override
	public void saveRep(Repository rep, ObjectId id_transformation,
			ObjectId id_step) throws KettleException {
		try {
			rep.saveStepAttribute(id_transformation, id_step, "file_name", fileName);
			rep.saveStepAttribute(id_transformation, id_step, "json_field_name", jsonFieldName); //$NON-NLS-1$
			rep.saveStepAttribute(id_transformation, id_step, "encoding", encoding);      
		} catch(KettleException e) {
			throw new KettleException(BaseMessages.getString(PKG, "MarcReaderMeta.Exception.UnableToSaveStepInfo")+id_step, e); //$NON-NLS-1$
		}		
	}
	
	
	public void getFields(RowMetaInterface rowMeta, String origin, RowMetaInterface[] info, StepMeta nextStep, VariableSpace space)
	throws KettleStepException {
	  ValueMetaInterface jsonValueMeta = new ValueMeta(jsonFieldName, ValueMetaInterface.TYPE_STRING);
	  jsonValueMeta.setOrigin(origin);
	  rowMeta.addValueMeta(jsonValueMeta);
	}
	
	public String getXML(){
	    StringBuffer retval = new StringBuffer(300);		
	    retval.append("    ").append(XMLHandler.addTagValue("file_name", fileName)); //$NON-NLS-1$ //$NON-NLS-2$
	    retval.append("    ").append(XMLHandler.addTagValue("json_field_name", jsonFieldName)); //$NON-NLS-1$ //$NON-NLS-2$
	    retval.append("    ").append(XMLHandler.addTagValue("encoding", encoding));
		return retval.toString();
	}
	
	public StepInterface getStep(StepMeta stepMeta, StepDataInterface stepDataInterface, int cnr, TransMeta tr, Trans trans){
		return new MarcReader(stepMeta, stepDataInterface, cnr, tr, trans);
	}
	
	public StepDataInterface getStepData(){
		return new MarcReaderData();
	}

	@Override
	public void readRep(Repository rep, ObjectId id_step,
			List<DatabaseMeta> databases, Map<String, Counter> counters)
			throws KettleException {
		try	{			
			fileName    = rep.getStepAttributeString (id_step, "file_name"); //$NON-NLS-1$
			jsonFieldName = rep.getStepAttributeString (id_step, "json_field_name"); //$NON-NLS-1$			
			encoding = rep.getStepAttributeString(id_step, "encoding");     
		}catch(Exception e)	{
			throw new KettleException(BaseMessages.getString(PKG, "MarcReaderMeta.Exception.UnexpectedErrorWhileReadingStepInfo"), e); //$NON-NLS-1$
		}
		
	}

	@Override
	public void check(List<CheckResultInterface> remarks, TransMeta transMeta,
			StepMeta stepMeta, RowMetaInterface prev, String[] input,
			String[] output, RowMetaInterface info) {
		// TODO Auto-generated method stub
		
	}
}
