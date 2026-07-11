import { useState } from 'react';

function App() {
  const [originalUrl, setOriginalUrl] = useState('');
  const [customCode, setCustomCode] = useState('');
  const [expirationTime, setExpirationTime] = useState('');
  const [shortUrl, setShortUrl] = useState('');
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState('');

  const [analyticsCode, setAnalyticsCode] = useState('');
  const [analyticsData, setAnalyticsData] = useState(null);
  const [analyticsError, setAnalyticsError] = useState('');

  const handleShorten = async (event) => {
    event.preventDefault();
    setLoading(true);
    setError('');
    setShortUrl('');

    try {
      const response = await fetch('/api/shorten', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify({
          url: originalUrl,
          customCode,
          expirationTime,
        }),
      });

      const responseText = await response.text();

      if (!response.ok) {
        throw new Error(responseText || 'Unable to shorten the URL right now.');
      }

      setShortUrl(responseText);
    } catch (err) {
      setError(err.message || 'Something went wrong.');
    } finally {
      setLoading(false);
    }
  };

  const handleAnalytics = async (event) => {
    event.preventDefault();
    setAnalyticsError('');
    setAnalyticsData(null);

    try {
      const response = await fetch(`/analytics/${analyticsCode}`);
      if (!response.ok) {
        throw new Error('Could not retrieve analytics for that short code.');
      }

      const data = await response.json();
      setAnalyticsData(data);
    } catch (err) {
      setAnalyticsError(err.message || 'Something went wrong.');
    }
  };

  return (
    <div className="app-shell">
      <header>
        <h1>URL Shortener</h1>
        <p>Create short links and check engagement in one place.</p>
      </header>

      <main className="card-grid">
        <section className="card">
          <h2>Shorten a URL</h2>
          <form onSubmit={handleShorten}>
            <label>
              Original URL
              <input
                type="url"
                value={originalUrl}
                onChange={(event) => setOriginalUrl(event.target.value)}
                placeholder="https://example.com"
                required
              />
            </label>

            <label>
              Custom code (optional)
              <input
                type="text"
                value={customCode}
                onChange={(event) => setCustomCode(event.target.value)}
                placeholder="my-link"
              />
            </label>

            <label>
              Expiration time (optional)
              <input
                type="datetime-local"
                value={expirationTime}
                onChange={(event) => setExpirationTime(event.target.value)}
                placeholder="2026-12-31"
              />
            </label>

            <button type="submit" disabled={loading}>
              {loading ? 'Creating...' : 'Shorten URL'}
            </button>
          </form>

          {error ? <p className="error">{error}</p> : null}

          {shortUrl ? (
            <div className="result-box">
              <h3>Your short link</h3>
              <a href={shortUrl} target="_blank" rel="noreferrer">
                {shortUrl}
              </a>
            </div>
          ) : null}
        </section>

        <section className="card">
          <h2>View analytics</h2>
          <form onSubmit={handleAnalytics}>
            <label>
              Short code
              <input
                type="text"
                value={analyticsCode}
                onChange={(event) => setAnalyticsCode(event.target.value)}
                placeholder="abc123"
                required
              />
            </label>
            <button type="submit">Get analytics</button>
          </form>

          {analyticsError ? <p className="error">{analyticsError}</p> : null}

          {analyticsData ? (
            <div className="result-box analytics">
              <p><strong>Original URL:</strong> {analyticsData.originalUrl}</p>
              <p><strong>Short code:</strong> {analyticsData.shortCode}</p>
              <p><strong>Clicks:</strong> {analyticsData.clickCount}</p>
              <p><strong>Created:</strong> {analyticsData.createdAt}</p>
              <p><strong>Expires:</strong> {analyticsData.expirationTime}</p>
            </div>
          ) : null}
        </section>
      </main>
    </div>
  );
}

export default App;
