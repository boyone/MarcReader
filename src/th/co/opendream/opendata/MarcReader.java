package th.co.opendream.opendata;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import net.sf.json.JSONObject;

import org.marc4j.DirtyMarcStreamReader;
import org.marc4j.marc.Record;
import org.pentaho.di.core.exception.KettleException;
import org.pentaho.di.core.row.RowDataUtil;
import org.pentaho.di.core.row.RowMeta;
import org.pentaho.di.trans.Trans;
import org.pentaho.di.trans.TransMeta;
import org.pentaho.di.trans.step.BaseStep;
import org.pentaho.di.trans.step.StepDataInterface;
import org.pentaho.di.trans.step.StepInterface;
import org.pentaho.di.trans.step.StepMeta;
import org.pentaho.di.trans.step.StepMetaInterface;

public class MarcReader extends BaseStep implements StepInterface {

	private static Class<?> PKG = MarcReaderMeta.class; // for i18n purposes, needed by Translator2!!   $NON-NLS-1$

	private MarcReaderMeta meta;
	private MarcReaderData data;
	
	public MarcReader(StepMeta stepMeta, StepDataInterface stepDataInterface,
			int copyNr, TransMeta transMeta, Trans trans) {
		super(stepMeta, stepDataInterface, copyNr, transMeta, trans);		
	}
	
	@Override
	public boolean init(StepMetaInterface smi, StepDataInterface sdi) {
		if(super.init(smi, sdi)){
			meta = (MarcReaderMeta) smi;
		  	data = (MarcReaderData) sdi;
		  	
		  	String fileName = environmentSubstitute(meta.getFileName());
		  	String encoding = environmentSubstitute(meta.getEncoding());
		  	
		  	try {
		  		data.input = new FileInputStream(new File(fileName));
		  		//if(dirty)
				data.reader = new DirtyMarcStreamReader(data.input,encoding);				
				
		  		return true;
		  	}catch (Exception e) {
		  		
				return false;
			}
		}else{
			return false;
		}
	}



	@Override
	public boolean processRow(StepMetaInterface smi, StepDataInterface sdi)
			throws KettleException {
		if (first) {
		    first=false;		    
		    data.outputRowMeta = new RowMeta();
		    meta.getFields(data.outputRowMeta,getStepname(), null, null, this);   		    
		}
		
		if (data.reader.hasNext() && !isStopped()) {
			Record record = data.reader.next();
			Map<String, Object> detail = new HashMap<String, Object>();
			detail.put("leader", record.getLeader());
			detail.put("controlFields", record.getControlFields());
			detail.put("dataFields", record.getDataFields());
			JSONObject jsonObject = JSONObject.fromObject(detail);
		    //String json = data.cursor.next().toString();
		    Object[] row = RowDataUtil.allocateRowData(data.outputRowMeta.size());
		    int index=0;
		    
		    
		    row[index++] = jsonObject.toString();

		    // putRow will send the row on to the default output hop.
		    //
		    putRow(data.outputRowMeta, row);
		    
		    return true;
		  } else {

		    setOutputDone();
	  
	  	  return false;
		  }
	}
	
	@Override
	public void dispose(StepMetaInterface smi, StepDataInterface sdi) {
		if(data.input!=null){
			try {
				data.input.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		super.dispose(smi, sdi);
	}

}
