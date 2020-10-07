<?php
//make sure rb.php is placed alongside this file. The following code will automatically create a
// table for you. All you need to do is first create a database,then provide the correct user credentials in your localhost.

	//we start by loading rb.php
    require 'rb.php';

    class Constants{
	//We specify table name. NB/= No special characters, no CamelCase letters
        static $TABLE_USER = 'canopustb';
    }

	    //TYPES OF DATABASE
    //1. REMOTE DB - Hosted online. Demo app uses our hosted demo database.
    //2. LOCALHOST - Hosted locally in your machine e.g XAMPP, WAMP
	R::setup( 'mysql:host=localhost;dbname=projectsdb', 'root', '' );

    class UMS{

        public function doesUserExist($email){

            $sql="SELECT * FROM ".Constants::$TABLE_USER." WHERE email='$email'";
            $user=R::getAll($sql);
            if(count($user) > 0){
                return true;
            }else{
                return false;
            }
        }
        public function registerUser($name,$email,$password,$securityQuestion){
            if($this->doesUserExist($email)){
                print(json_encode(array("code" => 2, "message"=>"This User Already Exists", "user" => null,"users" => null)));
                return;
            }
            $user = R::dispense(Constants::$TABLE_USER);
            $user->name = $name;
            $user->email = $email;
            $user->password = $password;
            $user->securityQuestion = $securityQuestion;
            $user->registrationDate = date("Y-m-d");
            $user->imageUrl = "";
            $user->meta = "";

            $id = R::store( $user );

            if($id > 0){
                $user->id = $id;
                print(json_encode(array("code" => 1, "message"=>"User Successfully Registered.",
                "user" => $user)));
            }else{
               print(json_encode(array("code" => 2, "message"=>"Not Registered  .", "user" => $user)));
            }

        }
        public function login($email,$password){
            if(!$this->doesUserExist($email)){
                print(json_encode(array("code" => 2, "message"=>"This user is not registered in the system")));
                return;
            }
            $sql="SELECT * FROM ".Constants::$TABLE_USER." WHERE email='$email' AND password='$password' LIMIT 1";
            $users=R::getAll($sql);
            if(count($users) > 0){
                print(json_encode(array("code" => 1, "message"=>"User Successfully Logged In.",
                 'user'=>$users[0])));
            }else{
                print(json_encode(array("code" => 2, "message"=>"Invalid Login Credentials.")));
            }
        }
        public function resetPassword($id,$newPassword){
            $user = R::load(Constants::$TABLE_USER,$id);
            $user->password = $newPassword;

            $id = R::store( $user );

            if($id > 0){
               print(json_encode(array("code" => 1, "message"=>"Password Successfully Reset.")));
            }else{
               print(json_encode(array("code" => 2, "message"=>"Password Not Reset  .")));
            }
        }
        public function resetEmail($id,$newEmail,$password){
            $user = R::load(Constants::$TABLE_USER,$id);
            if(!$user->password == $password){
                print(json_encode(array("code" => 2, "message"=>"Email Not Reset. Wrong Password.")));
                return;
            }
            $user->email = $newEmail;

            $id = R::store( $user );

            if($id > 0){
               print(json_encode(array("code" => 1, "message"=>"Email Successfully Reset.")));
            }else{
               print(json_encode(array("code" => 2, "message"=>"Email Not Reset  .")));
            }
        }

        public function updateOnlyText($id,$name,$securityQuestion){
            $user = R::load(Constants::$TABLE_USER,$id);
            $user->name = $name;
            $user->securityQuestion = $securityQuestion;
            $id = R::store( $user );

            if($id > 0){
                print(json_encode(array("code" => 1, "message"=>"User Text Successfully Updated.")));
            }else{
               print(json_encode(array("code" => 2, "message"=>"Not Updated  .")));
            }

        }

        public function deleteUser($id){
            $user = R::load( Constants::$TABLE_USER, $id );
            R::trash( $user);
            print(json_encode(array('code' =>1, 'message' => 'Delete Successful')));

        }

        public function selectById($id){
            $user = R::load( Constants::$TABLE_USER, $id );
            return $user;
        }
        public function selectAllUsers(){
            $users=R::getAll( 'SELECT * FROM '.Constants::$TABLE_USER );
            print(json_encode(array('code' =>1, 'message' => 'Data Successfully Fetched','users'=>$users)));
        }
        public function selectPaged($limit,$start){
            $users=R::getAll( 'SELECT * FROM '.Constants::$TABLE_USER. ' ORDER BY id DESC LIMIT '.$limit.' OFFSET '.$start);
             print(json_encode(array('code' =>1, 'message' => 'Data Successfully Fetched',
             'users'=>$users)));
        }
    }

    function handleRequest() {
        $u=new UMS();

        if($_SERVER['REQUEST_METHOD'] == 'POST')
        {
            if (isset($_POST['action'])) {

                $action=$_POST['action'];

                if($action == 'FETCH_ALL_USERS'){
                    $u->selectAllUsers();

				}else if($action == 'FETCH_PAGINATED_USERS'){
                    $limit = $_POST['limit'];
                    $start = $_POST['start'];
                    $u->selectPaged($limit,$start);

				}else if($action == 'CREATE_ACCOUNT'){
                    $name = $_POST['name'];
                    $email = $_POST['email'];
                    $password = $_POST['password'];
                    $securityQuestion = $_POST['security_question'];

                   $u->registerUser($name,$email,$password,$securityQuestion);

                }else if($action == 'UPDATE_ONLY_TEXT'){
                    $id = $_POST['id'];
                    $name = $_POST['name'];
                    $securityQuestion = $_POST['security_question'];

                   $u->updateOnlyText($id,$name,$securityQuestion);


                }else if($action == 'DELETE_ACCOUNT'){
                    $id = $_POST['id'];
                    $u->delete($id);


                }else if($action == 'RESET_PASSWORD'){
                    $id = $_POST['id'];
                    $passowrd = $_POST['password'];
                    $u->resetPassword($id,$password);

                }else if($action == 'LOGIN'){
                    $email = $_POST['email'];
                    $password = $_POST['password'];
                    $u->login($email,$password);


                }else{
				//if we don't know the request the user made
					print(json_encode(array('code' =>4, 'message' => 'INVALID REQUEST.')));
				}
            } else{
			//if we haven't accounted for the HTTP METHOD the user made
				print(json_encode(array('code' =>5, 'message' => 'POST TYPE UNKNOWN.')));
            }
        } else{

			//you can also create table by running the following command, then comment it
             //$u->registerUser("Paul Parketr","MALE","Hey there","USA","paulparker@gmail.com","123456","ADMIN","ACTIVE","1994-07-12","");

			 //Tetst Login
             //$u->login("johndoe@gmail.com","123456");

			 //Select 10 users
             $u->selectPaged("10","0");

        }

    }

    handleRequest();