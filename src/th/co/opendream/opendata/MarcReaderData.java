package th.co.opendream.opendata;

import java.io.InputStream;

import org.marc4j.MarcStreamReader;
import org.pentaho.di.core.row.RowMetaInterface;
import org.pentaho.di.trans.step.BaseStepData;
import org.pentaho.di.trans.step.StepDataInterface;

public class MarcReaderData extends BaseStepData implements StepDataInterface {
	public RowMetaInterface outputRowMeta;
	public MarcStreamReader reader;
	public InputStream input;	
}
