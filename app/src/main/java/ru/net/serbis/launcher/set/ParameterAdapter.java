package ru.net.serbis.launcher.set;

import android.content.*;
import android.text.*;
import android.view.*;
import android.widget.*;
import ru.net.serbis.launcher.*;
import ru.net.serbis.launcher.adapter.*;
import ru.net.serbis.launcher.help.*;

public class ParameterAdapter extends ItemAdapter<Parameter>
{
    public ParameterAdapter(Context context, int resourceId, Parameter[] objects)
    {
        super(context, resourceId, 0, objects);
    }

    @Override
    public View getView(int position, View view, ViewGroup parent)
    {
        Parameter parameter = getItem(position);

        view = getItemView(parameter.getType().getLayout());

        switch (parameter.getType())
        {
            case STRING:
                initText(view, parameter);
                break;
                
            case INTEGER:
                initSeek(view, parameter);
                break;

            case ORIENTATION:
                initOrientation(view, parameter);
                break;

            case BOOLEAN:
                initBoolean(view, parameter);
                break;
        }

        return view;
    }

    private void initText(View view, Parameter parameter)
    {
        initName(view, parameter);
        
        EditText edit = Tools.getView(view, R.id.value);
        edit.setText(parameter.getValue());
        
        addTextWatcher(edit, parameter);
    }
    
    private void initName(View view, Parameter parameter)
    {
        TextView name = Tools.getView(view, R.id.name);
        name.setText(parameter.getName().getResource());
    }
    
    private void initSeek(View view, Parameter parameter)
    {
        initName(view, parameter);
        
        SeekBar seek = Tools.getView(view, R.id.value);
        seek.setMax(parameter.getMax());
        seek.setProgress(parameter.getIntValue());
        
        TextView hint = Tools.getView(view, R.id.hint);
        hint.setText(parameter.getValue());
        
        setSeekChange(seek, hint, parameter);
    }
    
    private void initOrientation(View view, Parameter parameter)
    {
        initName(view, parameter);
        
        Spinner spinner = Tools.getView(view, R.id.value);
        ValueAdapter valueAdapter = new ValueAdapter(getContext(), parameter.getType().getLayout(), R.layout.value, parameter.getType().getValues()); 
        spinner.setAdapter(valueAdapter);
        int current = valueAdapter.getPosition(new Value(parameter.getValue(), 0));
        if (current > -1)
        {
            spinner.setSelection(current);
        }
        
        setSpinnerItemSelect(spinner, parameter);
    }

    private void initBoolean(View view, Parameter parameter)
    {
        CheckBox check = Tools.getView(view, R.id.value);
        check.setText(parameter.getName().getResource());
        check.setChecked(parameter.getBooleanValue());
        setCheckedChange(check, parameter);
    }

    private void addTextWatcher(EditText edit, final Parameter parameter)
    {
        edit.addTextChangedListener(
            new TextWatcher()
            {
                public void afterTextChanged(Editable edit)
                {
                    parameter.setValue(edit.toString());
                }

                public void beforeTextChanged(CharSequence chars, int start, int count, int after)
                { 
                }

                public void onTextChanged(CharSequence chars, int start, int before, int count)
                {
                }
            });
    }

    private void setSeekChange(SeekBar seek, final TextView hint, final Parameter parameter)
    {
        seek.setOnSeekBarChangeListener(
            new SeekBar.OnSeekBarChangeListener()
            {
                @Override
                public void onProgressChanged(SeekBar seek, int progress, boolean fromUser)
                {
                    if (progress < parameter.getMin())
                    {
                        progress = parameter.getMin();
                        seek.setProgress(parameter.getMin());
                    }
                    parameter.setIntValue(progress);
                    hint.setText(parameter.getValue());
                }
                
                @Override
                public void onStartTrackingTouch(SeekBar seek)
                {
                }
                
                @Override
                public void onStopTrackingTouch(SeekBar seek)
                {               
                }
            }
        );
    }
    
    private void setSpinnerItemSelect(Spinner spinner, final Parameter parameter)
    {
        spinner.setOnItemSelectedListener(
            new AdapterView.OnItemSelectedListener()
            {
                public void onItemSelected(AdapterView parent, View view, int position, long id)
                {
                    parameter.setValue(parameter.getType().getValues().get(position).getValue());
                }

                public void onNothingSelected(AdapterView parent)
                {
                }
            }
        );
    }

    private void setCheckedChange(CheckBox check, final Parameter parameter)
    {
        check.setOnCheckedChangeListener(
            new CompoundButton.OnCheckedChangeListener()
            {
                public void onCheckedChanged(CompoundButton button, boolean checked)
                {
                    parameter.setBooleanValue(checked);
                }
            }
        );
    }
}
