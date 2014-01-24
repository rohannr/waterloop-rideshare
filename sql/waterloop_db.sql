CREATE TABLE users (
	user_id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
	fb_id BIGINT NOT NULL,
	name VARCHAR(255) NOT NULL
);

CREATE TABLE rides (
	ride_id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
	source VARCHAR(63) NOT NULL,
	dest VARCHAR(63) NOT NULL,
	depart_time DATETIME NOT NULL,
	driver_id INT NOT NULL,
	num_seats TINYINT NOT NULL,
	price SMALLINT NOT NULL,
	FOREIGN KEY (driver_id) REFERENCES users(user_id)
);

CREATE TABLE ride_passengers (
	ride_passenger_id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
	ride_id INT,
	user_id INT,
	FOREIGN KEY (ride_id) REFERENCES rides(ride_id),
	FOREIGN KEY (user_id) REFERENCES users(user_id)
);

CREATE UNIQUE INDEX users_fb_id_idx ON users (fb_id);
CREATE UNIQUE INDEX ride_passengers_id ON ride_passengers(ride_id, user_id);