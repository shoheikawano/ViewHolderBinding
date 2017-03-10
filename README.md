# ViewHolderBinding

Layout and RecyclerView.ViewHolder binding which uses annotation processor to eliminate RecyclerView boilerplates for Android development.

- Eliminate `switch` or `if-else` statements for ViewType detection
- Eliminate layout inflation codes
- Eliminate casting RecyclerView.ViewHolder class for binding layouts and ViewHolders

# Usage

- Add `@AdapterBinding` to your custom RecyclerView.Adapter.
- Add `@ViewHolder` to inner ViewHolder classes to bind your layout and ViewHolder
- Override `getItemViewType` method to return `R.layout.your_layout` as ViewType
- (Optional) Add `@OnBind` to any of your inner method within your ViewHolder class annotated with `@ViewHolder` to get called on onBindViewHolder
- Rebuild project
- Call generated createViewHolder and bindViewHolder methods on onCreateViewHolder and onBindViewHolder, respectively

```java
@AdapterBinding class CatAdapter extends RecyclerView.Adapter {

  @Override public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    LayoutInflater inflater = LayoutInflater.from(parent.getContext());
    return CatAdapterViewHolderBinding.createViewHolder(inflater, parent, viewType);
  }

  @Override public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
    CatAdapterViewHolderBinding.bindViewHolder(this, holder, position);
  }

  @Override public int getItemViewType(int position) {
    // TODO Return Layout id(e.g. R.layout.your_layout) as view type
  }

  @Override public int getItemCount() {
    // TODO Return item count
  }

  @ViewHolder(layout = R.layout.row_taro) static class TaroViewHolder
      extends RecyclerView.ViewHolder {
    ...

    @OnBind void onBind(int position) {
      // Bind layout
    }
  }
}
```

See [CatAdapter](https://github.com/shaunkawano/ViewHolderBinding/blob/master/viewholderbinding-sample/src/main/java/com/shaunkawano/viewholderbinding/CatAdapter.java) to see more actual code as well.

## Download

```gradle
compile 'com.shaunkawano.viewholderbinding:viewholderbinding-annotations:0.5.1'
annotationProcessor 'com.shaunkawano.viewholderbinding:viewholderbinding-processor:0.5.1'
```

## License

```
Copyright 2017 Shohei Kawano

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

  http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```
