# jTwitterAPI

This is a simple set of Java classes to consume Twitter API. 

This project contains couple of generic classes to create new Twitter calls, quickly and easily. You will also find an example of how to use Twitter streaming calls.

## How to use it / Example

Follow this link to launch a simple example with only one main method: [https://github.com/thepocagency/com.tpa.opensource.twitterapi.examples](https://github.com/thepocagency/com.tpa.opensource.twitterapi.examples)

We try to facilitate and minimize your code, so you will be able to:

### To authenticate yourself on Twitter:

With only one line:

```
    TwitterAuthenticator twitterAuthenticator = new TwitterAuthenticator();
```

### To call a Twitter streaming URL:

To execute Twitter calls with chaining methods:

```
    List<TwitterStatus> statuses = new StreamingSearch(twitterAuthenticator, delayInSeconds, sizeLimit)              
            .addParameter("track", textSearch) // Optionnal (if missing: uses default text search)
            .executeListRequest(); // Executes the request
```

## Tests

First tests are implemented. We will try to increase test covering in the future.

## Credits

We'd like to thank you [Picnic](https://www.picnic.nl/) for this nice assignment :)

Developped by Alexandre Veremme @ [The POC Agency](https://www.the-poc-agency.com)

## Licence

Licensed under the Apache License, Version 2.0 (the "License")

You may not use this file except in compliance with the License

You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied

See the License for the specific language governing permissions and limitations under the License