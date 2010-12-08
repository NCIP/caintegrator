DELETE from microarray where id not in (SELECT microarray_id FROM microarray_samples);
ALTER table microarray drop sample_id;