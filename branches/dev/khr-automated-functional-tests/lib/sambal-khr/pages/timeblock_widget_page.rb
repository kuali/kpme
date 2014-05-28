class TimeblockWidgetPage < BasePage

  element(:start_date) { |b| b.text_field(id: 'startDate') }
  element(:end_date) { |b| b.text_field(id: 'endDate') }
  action(:add) { |b| b.button(text: 'Add').click }
  value(:validation_text) { |b| b.p(id: 'validation').text}
  element(:earn_code) { |b| b.select(id: 'selectedEarnCode')}
  action(:close) { |b| b.span(text: 'close').click}

  element(:assignment) { |b| b.select(id: 'selectedAssignment')}
  element(:in_time) { |b| b.text_field(id: 'startTimeHourMinute')}
  element(:out_time) { |b| b.text_field(id: 'endTimeHourMinute')}

  element(:earn_code_cal) { |b| b.select(id: 'selectedEarnCode').option(value: 'CAL')}


  def add_dates
    end_dt = end_date.value
    #puts "ed dt in date #{ed_dt}"
    date1 = Date.strptime(end_dt ,'%m/%d/%Y')
    start_dt = (date1 + 1).to_s
    #puts "st_dt is #{st_dt}"
    new_startdate = Date.strptime(start_dt, '%Y-%m-%d').strftime("%m/%d/%Y")
  end



end