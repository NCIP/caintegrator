ALTER TABLE SEGMENT_DATA ADD COLUMN CALLS_VALUE FLOAT AFTER END_POSITION,
 ADD COLUMN PROBABILITY_AMPLIFICATION FLOAT AFTER CALLS_VALUE,
 ADD COLUMN PROBABILITY_GAIN FLOAT AFTER PROBABILITY_AMPLIFICATION,
 ADD COLUMN PROBABILITY_LOSS FLOAT AFTER PROBABILITY_GAIN,
 ADD COLUMN PROBABILITY_NORMAL FLOAT AFTER PROBABILITY_LOSS;
