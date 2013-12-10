update segment_data set calls_value = 0 where calls_value is null;
update segment_data set probability_amplification = 0 where probability_amplification is null;
update segment_data set probability_gain = 0 where probability_gain is null;
update segment_data set probability_loss = 0 where probability_loss is null;
update segment_data set probability_normal = 0 where probability_normal is null;