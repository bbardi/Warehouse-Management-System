input metauml;
beginfig(1);
    Actor.user("User");
	Actor.db("Database");

	Usecase.dbquery("Queries database");

leftToRight(30)(user,dbquery,db);

drawObjects(user, dbquery, db);

clink(inheritance)(user.human, dbquery);
clink(association)(dbquery, db.human);
endfig;
end