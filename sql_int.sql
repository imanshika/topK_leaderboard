USE gamingservice;

CREATE TABLE player(
    player_id  VARCHAR(255) PRIMARY KEY,
    player_name VARCHAR(255) NOT NULL,
    registered_date datetime DEFAULT CURRENT_TIMESTAMP()
);

CREATE TABLE player_score (
  player_id VARCHAR(255) NOT NULL,
  score INT NOT NULL,
  KEY idx_score (score),
  KEY player_id (player_id),
  CONSTRAINT player_score_fk_1 FOREIGN KEY (player_id) REFERENCES player (player_id) ON DELETE CASCADE
);

drop procedure if exists insert_player;

delimiter #
create procedure insert_player()
begin

declare v_max int unsigned default 1000;
declare v_counter int unsigned default 1;
declare i varchar(1000);
declare player_id varchar(1000);
declare player_name varchar(1000);

  while v_counter <= v_max do
    set i = v_counter;
    set player_id =  CONCAT('Player-',i);
    set player_name = CONCAT('Player ',i);
    insert into player (player_id, player_name) values (player_id, player_name);
    set v_counter=v_counter+1;
  end while;
  commit;
end #

delimiter ;
   
call insert_player();

