# Project 4 - Instagram

Instagram is a photo sharing app using Parse as its backend.

Time spent: 18 hours spent in total

## User Stories

The following **required** functionality is completed:

- [x] User sees app icon in home screen.
- [x] User can sign up to create a new account using Parse authentication
- [x] User can log in and log out of his or her account
- [x] The current signed in user is persisted across app restarts
- [x] User can take a photo, add a caption, and post it to "Instagram"
- [x] User can view the last 20 posts submitted to "Instagram"
- [x] User can pull to refresh the last 20 posts submitted to "Instagram"
- [x] User can tap a post to view post details, including timestamp and caption.

The following **stretch** features are implemented:

- [x] Style the login page to look like the real Instagram login page.
- [x] Style the feed to look like the real Instagram feed.
- [x] User should switch between different tabs - viewing all posts (feed view), capture (camera and photo gallery view) and profile tabs (posts made) using fragments and a Bottom Navigation View.
- [ ] User can load more posts once he or she reaches the bottom of the feed using endless scrolling.
- [x] Show the username and creation time for each post
- [x] After the user submits a new post, show an indeterminate progress bar while the post is being uploaded to Parse
- User Profiles:
  - [ ] Allow the logged in user to add a profile photo
  - [ ] Display the profile photo with each post
  - [ ] Tapping on a post's username or profile photo goes to that user's profile page
  - [ ] User Profile shows posts in a grid view
- [ ] User can comment on a post and see all comments for each post in the post details screen.
- [x] User can like a post and see number of likes for each post in the post details screen.

The following **additional** features are implemented:

- [x] User can delete the photo they tap the x icon
- [x] Users can upload photos from their gallery
  - [ ] sending that photo to the server is a little iffy, hard to convert URI to a File
- [x] Users can click out of the details page with the fragment's surrounding space
- [x] Displays a shortened timestamp on the home page
  - [x] Displays a lengthened timestamp on the details fragment
- [x] Protected/hidden text field for passwords in the login and signup activities

Please list two areas of the assignment you'd like to **discuss further with your peers** during the next class (examples include better ways to implement something, how to extend your app in certain ways, etc):

1. When attempting to have users upload photos from their gallery, I had a lot of trouble converting an URI to a photo File. I found multiple solutions online, but using ContentResolvers and Cursors didn't work for me.
2. I would also love to discuss the different methods of transferring data between fragments. For my app, I had an onclick in my Adapter than spawned FragmentA, which upon exit should send data to Fragment B. I attempted to use a local broadcast manager and the setTargetFragment(), but both were iffy.

## Video Walkthrough

Here's a walkthrough of implemented user stories:

![Demo](https://github.com/aczoo/Instagram/blob/master/insta_demo.gif)

## Credits

List an 3rd party libraries, icons, graphics, or other assets you used in your app.

- [Android Async Http Client](http://loopj.com/android-async-http/) - networking library


## Notes
- Uploading stored media has been a dead end for me.
- A lot of similarities to Twitter. That being said, building this simplified Instagram really reinforces our understanding of what we had learned last week.

## License

    Copyright [2020] [?]

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

        http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
