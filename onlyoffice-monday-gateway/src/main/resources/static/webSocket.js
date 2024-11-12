function webSocketConfiguration() {
    return {
      isConnected: false,
      async getSessionToken() {
        try {
          const { data: sessionToken } = await mondaySdk().get("sessionToken");
          return sessionToken;
        } catch (error) {
          return null;
        }
      },
      async updateWebSocketConnection(sessionToken) {
        if (!sessionToken) return;
        const wsElement = this.$el.closest('[hx-ext]');
        if (wsElement) {
          wsElement.setAttribute("ws-connect", `/notifications?sessionToken=${sessionToken}`);
          htmx.process(wsElement);
        }
      },
      async init() {
        const sessionToken = await this.getSessionToken();
        if (sessionToken) {
          this.isConnected = true;
          await this.updateWebSocketConnection(sessionToken);
        }
        htmx.on('htmx:wsClose', async () => {
          this.isConnected = false;
          const newToken = await this.getSessionToken();
          if (newToken) {
            this.isConnected = true;
            await this.updateWebSocketConnection(newToken);
          }
        });
        htmx.on('htmx:wsOpen', () => {
          this.isConnected = true;
        });
      }
    }
  }