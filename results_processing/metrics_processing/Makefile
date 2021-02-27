PNG_WIDTH = 600
PNG_HEIGHT = 400

.PHONY: all
all: plot_all

.PHONY: clean
clean:
	rm *.pdf
	rm *.png

.PHONY: plot_all
plot_all: plot_data_item_churn plot_load plot_node_size

.PHONY: plot_data_item_churn
plot_data_item_churn: data_item_churn.pdf data_item_churn.png

data_item_churn.pdf: plot_data_item_churn.sh check_metrics_path
	bash plot_data_item_churn.sh $(METRICS_PATH) pdf

data_item_churn.png: plot_data_item_churn.sh check_metrics_path
	bash plot_data_item_churn.sh $(METRICS_PATH) png $(PNG_WIDTH) $(PNG_HEIGHT)

.PHONY: plot_load
plot_load: load.pdf load.png

load.pdf: plot_load.sh check_metrics_path
	bash plot_load.sh $(METRICS_PATH) pdf

load.png: plot_load.sh check_metrics_path
	bash plot_load.sh $(METRICS_PATH) png $(PNG_WIDTH) $(PNG_HEIGHT)

.PHONY: plot_node_size
plot_node_size: node_size.pdf node_size.png

node_size.pdf: plot_node_size.sh check_metrics_path
	bash plot_node_size.sh $(METRICS_PATH) pdf

node_size.png: plot_node_size.sh check_metrics_path
	bash plot_node_size.sh $(METRICS_PATH) png $(PNG_WIDTH) $(PNG_HEIGHT)

.PHONY: check_metrics_path
check_metrics_path:
ifndef METRICS_PATH
	$(error METRICS_PATH env variable is undefined)
endif
