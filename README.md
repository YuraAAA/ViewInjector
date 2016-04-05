# ViewInjector
Simple view injector for android

## Usage

### Dependency

Add this line to your dependencies

```
compile 'com.aizenberg:viewinjector:0.1.0'
```

### Inject view

Your activity xml

```xml
<?xml version="1.0" encoding="utf-8"?>
<LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
    android:orientation="vertical" android:layout_width="match_parent"
    android:layout_height="match_parent">

    <TextView
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:text="New Text"
        android:id="@+id/textView"
        android:layout_gravity="center_horizontal" />

    <Button
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:text="New Button"
        android:id="@+id/button"
        android:layout_gravity="center_horizontal" />

    <LinearLayout
        android:orientation="horizontal"
        android:layout_width="match_parent"
        android:layout_height="match_parent"
        android:layout_gravity="center_horizontal"
        android:id="@+id/container"/>
</LinearLayout>
```
Activity:

```java
@Id(R.id.textView)
private TextView textView;

@Override
protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    ViewInjector.reflect(this, this);
}
```

### Usage with fragments

1. Create base fragment

```java
public class BaseFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = ViewInjector.reflectLayout(this, inflater, container);
        afterViewsInjected(savedInstanceState);
        return view;

    }

    protected void afterViewsInjected(Bundle savedInstanceState) {

    }
}
```

2. Create your fragments:

```java
@Layout(R.layout.main_fragment)
public class MainFragment extends BaseFragment {

    private static final String TAG = MainFragment.class.getSimpleName();

    @Id(value = R.id.progressBar)
    private ProgressBar progressBar;

}
```

### Setup click listener

#### With field annotations

```java
public class MainActivity extends Activity {

    @Id(R.id.textView)
    private TextView textView;
    @Id(value = R.id.button, clickMethod = "btnClick")
    private Button button;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ViewInjector.reflect(this, this);
    }

    public void btnClick(View view) {
        Log.d(MainActivity.class.getSimpleName(), "Clicked");
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.container, MainFragment.instantiate(this, MainFragment.class.getName()));
        fragmentTransaction.commit();
    }
}
```

#### With method annotations

```java
@Layout(R.layout.main_fragment)
public class MainFragment extends BaseFragment {

    private static final String TAG = MainFragment.class.getSimpleName();

    @Id(value = R.id.progressBar)
    private ProgressBar progressBar;

    @Override
    protected void afterViewsInjected(Bundle savedInstanceState) {
        super.afterViewsInjected(savedInstanceState);
    }

    @Click(R.id.progressBar)
    public void progressClick(View view) {
        Log.d(TAG, "progressClick: ");
    }
}
```