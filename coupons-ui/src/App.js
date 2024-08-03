import { useEffect, useState } from "react";

function App() {
  const [code, setCode] = useState("");
  const [discount, setDiscount] = useState(0.0);
  const [expDate, setExpDate] = useState("");
  const [inputCode, setInputCode] = useState("");
  const [inputDiscount, setInputDiscount] = useState(0.0);
  const [inputExpDate, setInputExpDate] = useState("");
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(false);
  const [showCreateCoupon, setShowCreateCoupon] = useState(true);
  const [submitBtnDisabled, setSubmitBtnDisabled] = useState(true);
  useEffect(() => {
    async function fetchCoupon() {
      try {
        const fetchResponse = await fetch(
          `${process.env.REACT_APP_COUPONS_SERVICE_URL}/${process.env.REACT_APP_COUPON_CODE}`
        );
        const fetchJson = await fetchResponse.json();
        if (fetchResponse.ok) {
          setCode(fetchJson.code);
          setDiscount(fetchJson.discount);
          setExpDate(fetchJson.expDate);
        } else {
          setError(true);
        }
      } catch (ex) {
        console.error(`Error in calling /coupons api: ${ex}`);
        setError(true);
      }
      setLoading(false);
    }
    fetchCoupon();
  }, []);

  const handleSubmit = async (e) => {
    e.preventDefault();
    const body = {};
    if (
      inputCode &&
      inputCode.length > 3 &&
      inputDiscount > 10.0 &&
      inputExpDate.length === 10
    ) {
      body.code = inputCode;
      body.discount = inputDiscount;
      body.expDate = inputExpDate;
      setSubmitBtnDisabled(false);
    } else {
      setSubmitBtnDisabled(true);
      return;
    }
    const couponResponseDiv = document.getElementById("create-coupon-response");
    try {
      const couponCreateResponse = await fetch(
        process.env.REACT_APP_COUPONS_SERVICE_URL,
        {
          method: "POST",
          body: JSON.stringify(body),
          headers: {
            "Content-Type": "application/json",
          },
        }
      );
      const couponBody = await couponCreateResponse.json();
      couponResponseDiv.innerHTML = `<p style="color:green;">Coupon created successfully with id: ${couponBody.id}</p>`;
    } catch (ex) {
      couponResponseDiv.innerHTML = `<p style="color:red;">Failed to create the coupon!</p>`;
    }
    setShowCreateCoupon(false);
  };

  let response;
  if (loading) {
    response = (
      <div>
        <h2>API call is on its way!</h2>
        <p>Loading...</p>
      </div>
    );
  } else if (error) {
    response = (
      <div>
        <h2 style={{ color: "red" }}>
          {" "}
          Something went wrong, please try again!
        </h2>
      </div>
    );
  } else {
    response = (
      <div>
        <h2 style={{ color: "green" }}>
          {" "}
          Coupons details fetched successfully!
        </h2>
        <p>Following are the details: </p>
        <p>code: {code}</p>
        <p>discount: {discount}</p>
        <p>valid till: {expDate}</p>
      </div>
    );
  }
  const createCoupon = (
    <div>
      <h2> Create Coupon for your customers!</h2>
      {submitBtnDisabled && <b>Please fill all the details!</b>}
      <form onSubmit={handleSubmit}>
        <label htmlFor="code"> Code </label>
        <input
          id="code"
          name="code"
          type="text"
          value={inputCode}
          onChange={(e) => setInputCode(e.target.value)}
        ></input>
        <br />
        <label htmlFor="discount"> Discount</label>
        <input
          id="discount"
          name="discount"
          type="number"
          value={inputDiscount}
          onChange={(e) => setInputDiscount(Number(e.target.value))}
        ></input>
        <br />
        <label htmlFor="expDate"> Exp. Date</label>
        <input
          id="expDate"
          name="expDate"
          type="text"
          value={inputExpDate}
          onChange={(e) => setInputExpDate(e.target.value)}
        ></input>
        <br />
        <input type="submit"></input>
      </form>
    </div>
  );

  return (
    <div className="App">
      <h1>Welcome to Coupons UI</h1>
      {response}
      <br />
      {showCreateCoupon && createCoupon}
      <div id="create-coupon-response"></div>
    </div>
  );
}

export default App;
